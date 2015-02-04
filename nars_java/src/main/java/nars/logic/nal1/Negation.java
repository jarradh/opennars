/*
 * Negation.java
 *
 * Copyright (C) 2008  Pei Wang
 *
 * This file is part of Open-NARS.
 *
 * Open-NARS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Open-NARS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Open-NARS.  If not, see <http://www.gnu.org/licenses/>.
 */
package nars.logic.nal1;

import nars.logic.NALOperator;
import nars.logic.entity.CompoundTerm;
import nars.logic.entity.Term;

/**
 * A negation of a statement.
 */
public class Negation extends CompoundTerm {



    /** avoid using this externally, because double-negatives can be unwrapped to the 
     * original term using Negation.make */
    protected Negation(final Term t) {
        super(new Term[] { t });
        
        init(term);
    }

    @Override
    protected CharSequence makeName() {
        return makeCompoundName(NALOperator.NEGATION, negated());
    }


    @Override
    public int getMinimumRequiredComponents() {
        return 1;
    }
    
    /**
     * Clone an object
     *
     * @return A new object
     */
    @Override
    public Negation clone() {
        return new Negation(term[0]);
    }

    @Override
    public Term clone(final Term[] replaced) {
        if (replaced.length!=1)
            return null;
        return make(replaced[0]);
    }
    
    /** get the term which is negated by this */
    public Term negated() {
        return term[0];
    }

    /**
     * Try to make a Negation of one component. Called by the logic rules.
     *
     * @param t The component
     * @return A compound generated or a term it reduced to
     */
    public static Term make(final Term t) {
        if (t instanceof Negation) {
            // (--,(--,P)) = P
            return ((Negation) t).negated();
        }         
        return new Negation(t);
    }

    /**
     * Try to make a new Negation. Called by StringParser.
     *
     * @return the Term generated from the arguments
     * @param argument The list of term
     */
    public static Term make(final Term[] argument) {
        if (argument.length != 1)
            return null;        
        return make(argument[0]);
    }

    /**
     * Get the operator of the term.
     *
     * @return the operator of the term
     */
    @Override
    public NALOperator operator() {
        return NALOperator.NEGATION;
    }

    
    public static boolean areMutuallyInverse(Term a, Term b) {
        //doesnt seem necessary to check both, one seems sufficient.
        //incurs cost of creating a Negation and its id
        return (b.equals(Negation.make(a)) /* || tc.equals(Negation.make(ptc))*/ );
    }

    static boolean areMutuallyInverseNOTWORKINGYET(Term a, Term b) {
        boolean aNeg = a instanceof Negation;
        boolean bNeg = b instanceof Negation;

        if (aNeg && !bNeg)
            return areMutuallyInverse((Negation)a, b);
        else if (!aNeg && bNeg)
            return areMutuallyInverse((Negation)b, a);
        else
            return false;
    }

    static boolean areMutuallyInverse(Negation a, Term b) {
        return (a.negated().equals(b));
    }

}