//----------------------------------------------------------------------------//
//                                                                            //
//                 S y m b o l G l y p h D e s c r i p t o r                  //
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

import org.audiveris.omr.util.PointFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Point;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Class {@code SymbolGlyphDescriptor} brings additional information
 * to a mere shaped glyph.
 * <p>Such a descriptor contains a simple reference to the related shape,
 * whose appearance will be drawn thanks to MusicFont.
 * The descriptor can be augmented by informations such as stem number, with
 * ledger, pitch position, reference point.
 * These informations are thus copied to the {@link SymbolGlyph} instance for
 * better training.
 * We can have several descriptors from the same shape, which allows
 * different values for additional informations (for example, the stem
 * number may be 1 or 2 for NOTEHEAD_BLACK shape).
 *
 * @author Hervé Bitteur
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = {
    "xmlRefPoint", "stemNumber", "withLedger", "pitchPosition"})
@XmlRootElement(name = "symbol")
public class SymbolGlyphDescriptor
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            SymbolGlyphDescriptor.class);

    /** Un/marshalling context for use with JAXB */
    private static JAXBContext jaxbContext;

    //~ Instance fields --------------------------------------------------------
    /** Image related interline value */
    @XmlAttribute
    private Integer interline;

    /** Related name (generally the name of the related shape if any) */
    @XmlAttribute
    private String name;

    /** How many stems is it connected to ? */
    @XmlElement(name = "stem-number")
    private Integer stemNumber;

    /** Connected to Ledger ? */
    @XmlElement(name = "with-ledger")
    private Boolean withLedger;

    /** Pitch position within staff lines */
    @XmlElement(name = "pitch-position")
    private Double pitchPosition;

    /**
     * Reference point, if any. (Un)Marshalling is done through getXmlRefPoint()
     * and setXmlRefPoint().
     */
    private Point refPoint;

    //~ Constructors -----------------------------------------------------------
    //-----------------------//
    // SymbolGlyphDescriptor //
    //-----------------------//
    /**
     * No-arg constructor for the XML mapper
     */
    private SymbolGlyphDescriptor ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //-------------------//
    // loadFromXmlStream //
    //-------------------//
    /**
     * Load a symbol description from an XML stream.
     *
     * @param is the input stream
     * @return a new SymbolGlyphDescriptor, or null if loading has failed
     */
    public static SymbolGlyphDescriptor loadFromXmlStream (InputStream is)
    {
        try {
            return (SymbolGlyphDescriptor) jaxbUnmarshal(is);
        } catch (Exception ex) {
            ex.printStackTrace();

            // User already notified
            return null;
        }
    }

    //---------//
    // getName //
    //---------//
    /**
     * Report the name (generally the shape name) of the symbol
     *
     * @return the symbol name
     */
    public String getName ()
    {
        return name;
    }

    //------------------//
    // getPitchPosition //
    //------------------//
    /**
     * Report the pitch position within the staff lines
     *
     * @return the pitch position
     */
    public Double getPitchPosition ()
    {
        return pitchPosition;
    }

    //-------------//
    // getRefPoint //
    //-------------//
    /**
     * Report the assigned reference point
     *
     * @return the ref point, which may be null
     */
    public Point getRefPoint ()
    {
        return refPoint;
    }

    //---------------//
    // getStemNumber //
    //---------------//
    /**
     * Report the number of stems this entity is connected to
     *
     * @return the number of stems
     */
    public Integer getStemNumber ()
    {
        return stemNumber;
    }

    //--------------//
    // isWithLedger //
    //--------------//
    /**
     * Is this entity connected to a ledger
     *
     * @return true if there is at least one ledger
     */
    public Boolean isWithLedger ()
    {
        return withLedger;
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder("{");
        sb.append(getClass().getSimpleName());

        if (name != null) {
            sb.append(" name:")
                    .append(name);
        }

        if (interline != null) {
            sb.append(" interline:")
                    .append(interline);
        }

        if (stemNumber != null) {
            sb.append(" stem-number:")
                    .append(stemNumber);
        }

        if (withLedger != null) {
            sb.append(" with-ledger:")
                    .append(withLedger);
        }

        if (pitchPosition != null) {
            sb.append(" pitch-position:")
                    .append(pitchPosition);
        }

        sb.append("}");

        return sb.toString();
    }

    //----------------//
    // getJaxbContext //
    //----------------//
    private static JAXBContext getJaxbContext ()
            throws JAXBException
    {
        // Lazy creation
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance(SymbolGlyphDescriptor.class);
        }

        return jaxbContext;
    }

    //---------------//
    // jaxbUnmarshal //
    //---------------//
    private static Object jaxbUnmarshal (InputStream is)
            throws JAXBException
    {
        Unmarshaller um = getJaxbContext()
                .createUnmarshaller();

        return um.unmarshal(is);
    }

    //----------------//
    // getXmlRefPoint //
    //----------------//
    private PointFacade getXmlRefPoint ()
    {
        if (refPoint != null) {
            return new PointFacade(refPoint);
        } else {
            return null;
        }
    }

    //----------------//
    // setXmlRefPoint //
    //----------------//
    @XmlElement(name = "ref-point")
    private void setXmlRefPoint (PointFacade xp)
    {
        refPoint = xp.getPoint();
    }
}
