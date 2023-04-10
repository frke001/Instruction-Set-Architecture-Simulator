package CodeAnalysis;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class ByteCodeGenerator {

    public static HashMap<String,Long> labelsToAddresses = new HashMap<>(); // we store an address for each label

    public static void generateByteCode(long startAdress){

        HashMap<String,Long> notDefinedLabels = new HashMap<>();

        for(String line : AssemblyCodeReader.assemblyCode){
            line = line.trim();
            line = line.toUpperCase();
            if(!line.contains(" ")){ // breakpoint (otherwise we have an instruction)
                if(Interpreter.keyWords.contains(line)){
                    Interpreter.addressSpace.put(startAdress++,(byte)Interpreter.keyWordToByteCode.get(line)); // we place the bytecode and keywords in the address space
                }else if(!Interpreter.keyWords.contains(line) && line.endsWith(":")){ // label
                    labelsToAddresses.put(line.substring(0,line.length()-1),startAdress);
                }
            }else{
                line = line.replaceAll("\\s+"," ").replaceAll("\\s*,\\s+",","); // eliminate excess whitespace

                String[] elements = line.split(" ");
                Interpreter.addressSpace.put(startAdress++,Interpreter.keyWordToByteCode.get(elements[0]));

                if("MUL".equals(elements[0]) || "DIV".equals(elements[0])) {
                    Interpreter.addressSpace.put(startAdress++, Interpreter.keyWordToByteCode.get(Interpreter.register));
                    Interpreter.addressSpace.put(startAdress++, Interpreter.keyWordToByteCode.get("RAX")); // before each register we put REGISTER to recognize it
                }
                String operands[] = elements[1].split(",");
                for(var op : operands){

                    if(Interpreter.registerNames.contains(op)){ // the operand is a register
                        Interpreter.addressSpace.put(startAdress++,Interpreter.keyWordToByteCode.get(Interpreter.register));
                        Interpreter.addressSpace.put(startAdress++,Interpreter.keyWordToByteCode.get(op));

                    }else if(op.startsWith("[") && op.endsWith("]")){
                        op = op.substring(1,op.length()-1);

                        if(Interpreter.registerNames.contains(op)){ // [reg]
                            Interpreter.addressSpace.put(startAdress++,Interpreter.keyWordToByteCode.get(Interpreter.indirectAdressing));
                            Interpreter.addressSpace.put(startAdress++,Interpreter.keyWordToByteCode.get(op));

                        }else{ //[address]

                            Interpreter.addressSpace.put(startAdress++,Interpreter.keyWordToByteCode.get(Interpreter.address));
                            long address = getLong(op);
                            byte[] addressBytes = getBytes(address);
                            for(var b : addressBytes) {
                                Interpreter.addressSpace.put(startAdress++, b); // write byte by byte in the address space
                            }
                        }

                    }else if(isNumber(op)){
                        Interpreter.addressSpace.put(startAdress++,Interpreter.keyWordToByteCode.get(Interpreter.constant));
                        long number = getLong(op);
                        byte[] numberBytes = getBytes(number);
                        for(var b : numberBytes)
                            Interpreter.addressSpace.put(startAdress++, b); // byte by byte of number

                    }else{

                        Interpreter.addressSpace.put(startAdress++,Interpreter.keyWordToByteCode.get(Interpreter.label));
                        if(labelsToAddresses.containsKey(op)){ // if the label is already defined, we write that address (which the label represents) into the memory because it is already written in the hash map
                            long address = labelsToAddresses.get(op);
                            byte[] addressBytes = getBytes(address);
                            for(var b : addressBytes)
                                Interpreter.addressSpace.put(startAdress++, b);

                        }else{ // if the definition of the label is only later, we put the label and the current address where we are in the auxiliary hash map
                            notDefinedLabels.put(op,startAdress); // remember the empty space where the address of the label begins
                            startAdress+=8; // let's leave a space for the address we're jumping to, and we'll fill it in later
                        }
                    }
                }
            }

        } // after the code we fill the empty places in the memory
        // we save the current address for writing the last byte
        // we fill the empty places in the memory because we know the addresses of the beginning of the empty places via another hashmap
        // addressForJump is the address we need to jump to, and we place that address (8B) on the empty address space
        // from the location we get for each label in the second hashmap
        // startAddress inside the loop is only a temporary value of the address from which we write into the memory
        long temporary = startAdress;
        for(var label : notDefinedLabels.entrySet()){
            long addressForJump = labelsToAddresses.get(label.getKey());
            startAdress=label.getValue();
            byte[] addressBytes = getBytes(addressForJump);
            for(var b : addressBytes)
                Interpreter.addressSpace.put(startAdress++, b);
        }
        startAdress=temporary; // go back to an end
        Interpreter.addressSpace.put(startAdress++,(byte)0);
    }
    public static boolean isNumber(String value){
        try {
            if(value.startsWith("0x") | value.startsWith("0X")){ // to recognize the address as a number
                value=value.substring(2,value.length());
                Long.parseLong(value,16); // viewed as a hex
            }else
                Long.parseLong(value);
            return true;
        }catch (Exception ex){
            return false;
        }
    }
    private static long getLong(String value){ // string hex into long
        if(value.startsWith("0x") || value.startsWith("0X")) {
            value = value.substring(2, value.length());
            return Long.parseLong(value, 16);
        }else
            return Long.parseLong(value);
    }
    private static void putLong(long value){

    }
    private static byte[] getBytes(long value){ // niz bajtova iz long vrijednosti
        return ByteBuffer.allocate(8).putLong(value).array();
    }
}
