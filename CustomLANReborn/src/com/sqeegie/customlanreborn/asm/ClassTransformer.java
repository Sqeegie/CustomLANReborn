package com.sqeegie.customlanreborn.asm;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import com.sqeegie.customlanreborn.core.CustomLANReborn;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		
		/*
		if (transformedName.equals("net.minecraft.command.ServerCommandManager") || transformedName.equals("bl")) { // 1.7.10 obf name
			return patchHelpCommand(basicClass);
		}	
		*/	
		return null;
	}
	/*
	public byte[] patchHelpCommand(byte[] basicClass) {
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		
		for (MethodNode m : classNode.methods) {
			if (m.name.equals("ServerCommandManager")) {
				CustomLANReborn.logger.info("In target method " + m.name + ":" + m.desc + ". Patching!");
				
                AbstractInsnNode targetNode = null;
                Iterator<AbstractInsnNode> iter = m.instructions.iterator();
               
                while (iter.hasNext()) {
                    // Check all nodes
                    targetNode = iter.next();

                    // Find the first INVOKESTATIC node
					if (targetNode.getOpcode() == Opcodes.INVOKESTATIC) {
						
                        iter.remove(targetNode.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious());
                        targetNode = iter.next(); // Select next node as target
                        break;
					}
                }
			}
		}
		CustomLANReborn.logger.info("Finished ASM Patching for LOTR!");
		return basicClass;
	}
	*/
}
