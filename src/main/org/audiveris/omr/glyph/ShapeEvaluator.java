//----------------------------------------------------------------------------//
//                                                                            //
//                        S h a p e E v a l u a t o r                         //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//
// Copyright © Hervé Bitteur and others 2000-2017. All rights reserved.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//----------------------------------------------------------------------------//
// </editor-fold>
package org.audiveris.omr.glyph;

import org.audiveris.omr.glyph.facets.Glyph;

import org.audiveris.omr.sheet.SystemInfo;

import org.audiveris.omr.util.Predicate;

import java.util.EnumSet;

/**
 * Interface {@code ShapeEvaluator} defines the features of a glyph
 * shape evaluator.
 *
 * @author Hervé Bitteur
 */
public interface ShapeEvaluator
{
    //~ Static fields/initializers ---------------------------------------------

    /** Empty conditions set */
    public static final EnumSet<Condition> NO_CONDITIONS = EnumSet.noneOf(
            Condition.class);

    //~ Enumerations -----------------------------------------------------------
    /** Conditions for evaluation */
    public static enum Condition
    {
        //~ Enumeration constant initializers ----------------------------------

        /** Make sure the shape is not blacklisted by the glyph at hand */
        ALLOWED,
        /** Make
         * sure all specific checks are successfully passed */
        CHECKED;

    }

    //~ Methods ----------------------------------------------------------------
    /**
     * Report the sorted sequence of best evaluation(s) found by the
     * evaluator on the provided glyph.
     *
     * @param glyph      the glyph to evaluate
     * @param system     the system containing the glyph to evaluate
     * @param count      the desired maximum sequence length
     * @param minGrade   the minimum evaluation grade to be acceptable
     * @param conditions optional conditions, perhaps empty
     * @param predicate  filter for acceptable shapes, perhaps null
     * @return the sequence of evaluations, perhaps empty
     */
    Evaluation[] evaluate (Glyph glyph,
                           SystemInfo system,
                           int count,
                           double minGrade,
                           EnumSet<Condition> conditions,
                           Predicate<Shape> predicate);

    /**
     * Report the name of this evaluator.
     *
     * @return the evaluator declared name
     */
    String getName ();

    /**
     * Use a threshold on glyph weight, to tell if the provided glyph
     * is just {@link Shape#NOISE}, or a real glyph.
     *
     * @param glyph the glyph to be checked
     * @return true if not noise, false otherwise
     */
    boolean isBigEnough (Glyph glyph);

    /**
     * Report the best evaluation for the provided glyph, above a
     * minimum grade value, among the shapes (non checked, but allowed)
     * that match the provided predicate.
     *
     * @param glyph     the glyph to evaluate
     * @param minGrade  the minimum evaluation grade to be acceptable
     * @param predicate filter for acceptable shapes, perhaps null
     * @return the best acceptable evaluation, or null if none
     */
    Evaluation rawVote (Glyph glyph,
                        double minGrade,
                        Predicate<Shape> predicate);

    /**
     * Report the best of all evaluations found by the evaluator on the
     * provided glyph, under the ALLOWED and CHECKED conditions.
     *
     * @param glyph    the glyph to evaluate
     * @param system   the system containing the glyph to evaluate
     * @param minGrade the minimum evaluation grade to be acceptable
     * @return the best acceptable evaluation, or null if none
     */
    Evaluation vote (Glyph glyph,
                     SystemInfo system,
                     double minGrade);

    /**
     * Report the best of all evaluations found by the evaluator on the
     * provided glyph, matching the optional conditions and the
     * provided predicate.
     *
     * @param glyph      the glyph to evaluate
     * @param system     the system containing the glyph to evaluate
     * @param minGrade   the minimum evaluation grade to be acceptable
     * @param conditions optional conditions, perhaps empty
     * @param predicate  filter for acceptable shapes, perhaps null
     * @return the best acceptable evaluation, or null if none
     */
    Evaluation vote (Glyph glyph,
                     SystemInfo system,
                     double minGrade,
                     EnumSet<Condition> conditions,
                     Predicate<Shape> predicate);

    /**
     * Report the best of all evaluations found by the evaluator on the
     * provided glyph, under the ALLOWED and CHECKED conditions and
     * matching the provided predicate.
     *
     * @param glyph     the glyph to evaluate
     * @param system    the system containing the glyph to evaluate
     * @param minGrade  the minimum evaluation grade to be acceptable
     * @param predicate filter for acceptable shapes, perhaps null
     * @return the best acceptable evaluation, or null if none
     */
    Evaluation vote (Glyph glyph,
                     SystemInfo system,
                     double minGrade,
                     Predicate<Shape> predicate);
}
