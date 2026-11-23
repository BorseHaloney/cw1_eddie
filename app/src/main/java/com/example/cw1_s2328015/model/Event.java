package com.example.cw1_s2328015.model;

import android.util.Log;


public class Event
{
    // which feed the event came from
    public enum Category {
        UNPLANNED,
        ROADWORKS
    }

    private Category category;
    private String id;
    private String recordType;
    private String comment;
    private String startTimeText;
    private String startTime;
    private String endTimeText;
    private String endTime;

    private String versionTimeText;     //when the record was last updated
    private String versionTime;         // broken down from versionTimeText
    private String probability;
    private String sourceType;
    private String direction;
    private String roadName;
    private String fromName;
    private String toName;
    private double fromLatitude = Double.NaN;
    private double fromLongitude = Double.NaN;
    private double toLatitude = Double.NaN;
    private double toLongitude = Double.NaN;
    private int operationalLanes = -1;
    private int lanesRestricted = -1;
    private String managementType;
    private String abnormalTrafficType;
    private String delaysType;
    private String maintenanceType;


    public Event() {
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStartTimeText() {
        return startTimeText;
    }

    public String getStartTime() {
        return startTime;
    }



    //stores start time string
    public void setStartTime(String startTimeText) {
        this.startTimeText = startTimeText;
        this.startTime = breakDownDateTime(startTimeText);

    }

    public String getEndTimeText() {
        return endTimeText;
    }

    public String getEndTime() {
        return endTime;
    }




    public void setEndTime(String endTimeText) {
        this.endTimeText = endTimeText;
        this.endTime = breakDownDateTime(endTimeText);

    }

    public String getVersionTimeText() {
        return versionTimeText;
    }


    public String getVersionTime() {
        return versionTime;
    }


    public void setVersionTime(String versionTimeText) {
        this.versionTimeText = versionTimeText;
        this.versionTime = breakDownDateTime(versionTimeText);
    }

    //returns the text unchanged if the format is unexpected
    private String breakDownDateTime(String text) {
        try {
            String t = text.trim();
            String year = t.substring(0, 4);
            String month = t.substring(5, 7);
            String day = t.substring(8, 10);
            String time = t.substring(11, 16);
            return day + "/" + month + "/" + year + " " + time;
        } catch (StringIndexOutOfBoundsException e) {
            Log.e("Event", "Error breaking down date/time: " + text);
            return text;
        }
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getFromName() {
        return fromName;
    }



    //this method stores the from place name
    public void setFromName(String fromName) {
        this.fromName = fromName;
        if (roadName == null) {
            this.roadName = extractRoad(fromName);
        }
    }

    public String getToName() {
        return toName;
    }


    //this method stores the to place name
    public void setToName(String toName) {
        this.toName = toName;
        this.roadName = extractRoad(toName);
    }

    // gets first word of a place name using regex
    private String extractRoad(String placeName) {
        return placeName.trim().split("[ /]|(?<=[0-9])(?=[A-Z])")[0];
    }

    public double getFromLatitude() {
        return fromLatitude;
    }

    public void setFromLatitude(String fromLatitude) {
        this.fromLatitude = parseCoordinate(fromLatitude);
    }

    public double getFromLongitude() {
        return fromLongitude;
    }

    public void setFromLongitude(String fromLongitude) {
        this.fromLongitude = parseCoordinate(fromLongitude);
    }

    public double getToLatitude() {
        return toLatitude;
    }

    public void setToLatitude(String toLatitude) {
        this.toLatitude = parseCoordinate(toLatitude);
    }

    public double getToLongitude() {
        return toLongitude;
    }

    public void setToLongitude(String toLongitude) {
        this.toLongitude = parseCoordinate(toLongitude);
    }

    public boolean hasFromPoint() {
        return !Double.isNaN(fromLatitude) && !Double.isNaN(fromLongitude);
    }

    public boolean hasToPoint() {
        return !Double.isNaN(toLatitude) && !Double.isNaN(toLongitude);
    }

    public int getOperationalLanes() {
        return operationalLanes;
    }


    //parses number of operational lanes
    public void setOperationalLanes(String operationalLanes) {
        try {
            this.operationalLanes = Integer.parseInt(operationalLanes.trim());
        } catch (NumberFormatException e) {
            Log.e("Event", "Error parsing operational lanes: " + operationalLanes);
        }
    }

    public int getLanesRestricted() {
        return lanesRestricted;
    }


    //parses number of restrited lanes
    public void setLanesRestricted(String lanesRestricted) {
        try {
            this.lanesRestricted = Integer.parseInt(lanesRestricted.trim());
        } catch (NumberFormatException e) {
            Log.e("Event", "Error parsing lanes restricted: " + lanesRestricted);
        }
    }

    public String getManagementType() {
        return managementType;
    }

    public void setManagementType(String managementType) {
        this.managementType = managementType;
    }

    public String getAbnormalTrafficType() {
        return abnormalTrafficType;
    }

    public void setAbnormalTrafficType(String abnormalTrafficType) {
        this.abnormalTrafficType = abnormalTrafficType;
    }

    public String getDelaysType() {
        return delaysType;
    }

    public void setDelaysType(String delaysType) {
        this.delaysType = delaysType;
    }

    // true if delaysType is "longDelays" (for colour coding)
    public boolean hasLongDelays() {
        return "longDelays".equalsIgnoreCase(delaysType);
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }




    public String getMaintenanceTypeText() {
        return toWords(maintenanceType);
    }

    public String getRoadAndDirectionText() {
        return roadName + " – " + toWords(direction);
    }

    // impact part of the expanded app
    public String getImpactText() {
        StringBuilder impact = new StringBuilder();
        addPart(impact, toWords(managementType));
        addPart(impact, toWords(abnormalTrafficType));
        if (operationalLanes == 0) {
            addPart(impact, "no lanes open");
        } else if (operationalLanes > 0) {
            addPart(impact, operationalLanes + (operationalLanes == 1 ? " lane open" : " lanes open"));
        }
        if (lanesRestricted > 0) {
            addPart(impact, lanesRestricted + (lanesRestricted == 1 ? " lane restricted" : " lanes restricted"));
        }
        addPart(impact, toWords(delaysType));
        if (impact.length() == 0) {
            return null;
        }
        return impact.toString();
    }

    // from to coordinates on two lines
    public String getCoordinatesText() {
        String text = "";
        if (hasFromPoint()) {
            text = "From: " + fromLatitude + ", " + fromLongitude;
        }
        if (hasToPoint()) {
            if (!text.isEmpty()) {
                text = text + "\n";
            }
            text = text + "To: " + toLatitude + ", " + toLongitude;
        }
        if (text.isEmpty()) {
            return null;
        }
        return text;
    }

    private void addPart(StringBuilder list, String part) {
        if (part == null || part.isEmpty()) {
            return;
        }
        if (list.length() > 0) {
            list.append(", ");
        }
        list.append(part);
    }


    //converts camel case into normal workds with the first words first letter capitalised
    private String toWords(String input) {
        if (input == null || input.isEmpty()) return input;
        String result = String.join(" ",
                input.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])"))
                .toLowerCase();
        return Character.toUpperCase(result.charAt(0)) + result.substring(1);
    }

    private double parseCoordinate(String text) {
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            Log.e("Event", "Error parsing coordinate: " + text);
            return Double.NaN;
        }
    }

    @Override
    public String toString() {
        return "Event{" +
                "category=" + category +
                ", id='" + id + '\'' +
                ", recordType='" + recordType + '\'' +
                ", comment='" + comment + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", versionTime=" + versionTime +
                ", probability='" + probability + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", direction='" + direction + '\'' +
                ", roadName='" + roadName + '\'' +
                ", fromName='" + fromName + '\'' +
                ", toName='" + toName + '\'' +
                ", from=(" + fromLatitude + ", " + fromLongitude + ")" +
                ", to=(" + toLatitude + ", " + toLongitude + ")" +
                ", operationalLanes=" + operationalLanes +
                ", lanesRestricted=" + lanesRestricted +
                ", managementType='" + managementType + '\'' +
                ", abnormalTrafficType='" + abnormalTrafficType + '\'' +
                ", delaysType='" + delaysType + '\'' +
                ", maintenanceType='" + maintenanceType + '\'' +
                '}';
    }
}