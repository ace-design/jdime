/*******************************************************************************
 * Copyright (c) 2013 Olaf Lessenich.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Olaf Lessenich - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package de.fosd.jdime.strategy;

import java.io.IOException;

import de.fosd.jdime.common.ASTNodeArtifact;
import de.fosd.jdime.common.FileArtifact;
import de.fosd.jdime.common.MergeContext;
import de.fosd.jdime.common.MergeTriple;
import de.fosd.jdime.common.NotYetImplementedException;
import de.fosd.jdime.common.operations.MergeOperation;

/**
 * Performs a structured merge.
 * 
 * @author Olaf Lessenich
 * 
 */
public class StructuredStrategy extends MergeStrategy<FileArtifact> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.fosd.jdime.strategy.MergeStrategy#merge(
	 * de.fosd.jdime.common.operations.MergeOperation,
	 * de.fosd.jdime.common.MergeContext)
	 */
	@Override
	public final void merge(final MergeOperation<FileArtifact> operation,
			final MergeContext context) throws IOException, 
			InterruptedException {

		assert (operation != null);
		assert (context != null);

		MergeTriple<FileArtifact> triple = operation.getMergeTriple();

		assert (triple != null);
		assert (triple.isValid()) : "The merge triple is not valid!";
		assert (triple.getLeft() instanceof FileArtifact);
		assert (triple.getBase() instanceof FileArtifact);
		assert (triple.getRight() instanceof FileArtifact);
				
		assert (triple.getLeft().exists() && !triple.getLeft().isDirectory());
		assert ((triple.getBase().exists() && !triple.getBase().isDirectory()) 
				|| triple.getBase().isEmptyDummy());
		assert (triple.getRight().exists() && !triple.getRight().isDirectory());

		context.resetStreams();

		FileArtifact target = null;

		if (operation.getTarget() != null) {
			assert (operation.getTarget() instanceof FileArtifact);
			target = (FileArtifact) operation.getTarget();
			assert (!target.exists() || target.isEmpty()) 
					: "Would be overwritten: " + target;
		}

		// ASTNodeArtifacts are created from the input files.
		// Then, a ASTNodeStrategy can be applied.
		// The Result is pretty printed and can be written into the output file.
		
		ASTNodeArtifact left, base, right;

		left = new ASTNodeArtifact(triple.getLeft());
		base = new ASTNodeArtifact(triple.getBase());
		right = new ASTNodeArtifact(triple.getRight());
		ASTNodeArtifact targetNode = left.createEmptyDummy();
		
		MergeTriple<ASTNodeArtifact> nodeTriple 
					= new MergeTriple<ASTNodeArtifact>(triple.getMergeType(), 
							left, base, right);
				
		MergeOperation<ASTNodeArtifact> astMergeOp 
				= new MergeOperation<ASTNodeArtifact>(nodeTriple, targetNode);
		
		astMergeOp.apply(context);
		
		if (context.hasErrors()) {
			System.err.println(context.getStdErr());
		}

		// write output
		if (target != null) {
			assert (target.exists());
			target.write(context.getStdIn());
		}

		// FIXME: remove me when implementation is complete!
		throw new NotYetImplementedException(
				"StructuredStrategy: Implement me!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.fosd.jdime.strategy.MergeStrategy#toString()
	 */
	@Override
	public final String toString() {
		return "structured";
	}

}