package com.example.listview;

import java.text.DecimalFormat;

/**
 * See builderpattern example project for how to do builders
 * they are essential when constructing complicated objects and
 * with many optional fields
 */
public class BikeData {
    public static final int COMPANY = 0;
    public static final int MODEL = 1;
    public static final int PRICE = 2;
    public static final int LOCATION = 3;

    //TODO make all BikeData fields final

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Company:" + Company + "\n" +
                "Model:" + Model + "\n" +
                "Price:" + Price + "\n" +
                "Location:" + Location + "\n" +
                "Date Listed:"+ Date + "\n" +
                "Description:"+ Description + "\n" +
                "Link:" + Link + "\n";
    }

    public String getPicture() {
        return this.Picture;
    }

    private String Company;
    private String Model;
    private Double Price;
    String Description;
    String Location;
    String Date;
    String Picture;
    String Link;

    private BikeData(Builder b) {
        this.Company     = b.Company;
        this.Model       = b.Model;
        this.Price       = b.Price;
        this.Description = b.Description;
        this.Location    = b.Location;
        this.Date        = b.Date;
        this.Picture     = b.Picture;
        this.Link        = b.Link;
    }

    /**
     * @author lynn builder pattern, see page 11 Effective Java UserData mydata
     *         = new
     *         UserData.Builder(first,last).addProject(proj1).addProject(proj2
     *         ).build()
     */
    public static class Builder {
        final String Company;
        final String Model;
        final Double Price;
        String Description;
        String Location;
        String Date;
        String Picture;
        String Link;

        // Model and price required
        Builder(String Company, String Model, Double Price) {
            this.Company = Company;
            this.Model = Model;
            this.Price = Price;
        }

        // the following are setters
        // notice it returns this bulder
        // makes it suitable for chaining
        Builder setDescription(String Description) {
            this.Description = (Description == null || Description.equals(""))? "none" : Description;
            return this;
        }

        Builder setLocation(String Location) {
            this.Location = (Location == null || Location.equals(""))? "Unknown" : Location;
            return this;
        }

        Builder setDate(String Date) {
            this.Date = (Date == null || Date.equals(""))? "No Date" : Date;
            return this;
        }

        Builder setPicture(String Picture) {
            this.Picture = (Picture == null || Picture.equals(""))? "No picture" : Picture;
            return this;
        }

        Builder setLink(String Link) {
            this.Link = (Link == null || Link.equals(""))? "No link" : Link;
            return this;
        }

        // use this to actually construct Bikedata
        // without fear of partial construction
        public BikeData build() {
            return new BikeData(this);
        }

    }
}
