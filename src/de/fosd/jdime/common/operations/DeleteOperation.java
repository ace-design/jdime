/*******************************************************************************
 * Copyright (C) 2013 Olaf Lessenich.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 *
 * Contributors:
 *     Olaf Lessenich - initial API and implementation
 ******************************************************************************/
package de.fosd.jdime.common.operations;

import de.fosd.jdime.common.Artifact;
import de.fosd.jdime.common.MergeContext;
import de.fosd.jdime.stats.Stats;
import de.fosd.jdime.stats.StatsElement;
import org.apache.log4j.Logger;

/**
 * The operation deletes
 * <code>Artifact</code>s.
 *
 * @author Olaf Lessenich
 *
 * @param <T> type of artifact
 *
 */
public class DeleteOperation<T extends Artifact<T>> extends Operation<T> {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(DeleteOperation.class);
    /**
     * The
     * <code>Artifact</code> that is deleted by the operation.
     */
    private T artifact;

    /**
     * Class constructor.
     *
     * @param artifact that is deleted by the operation
     */
    public DeleteOperation(final T artifact) {
        super();
        this.artifact = artifact;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.fosd.jdime.common.operations.Operation#apply()
     */
    @Override
    public final void apply(final MergeContext context) {
        assert (artifact != null);
        assert (artifact.exists()) : "Artifact does not exist: " + artifact;

        if (LOG.isDebugEnabled()) {
            LOG.debug("Applying: " + this);
        }

        if (context.hasStats()) {
            Stats stats = context.getStats();
            stats.incrementOperation(this);
            StatsElement element = stats.getElement(
                    artifact.getStatsKey(context));
            element.incrementDeleted();
        }
    }

    @Override
    public final String getName() {
        return "DELETE";
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return getId() + ": " + getName() + " " + artifact;
    }
}
