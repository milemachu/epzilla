package org.epzilla.dispatcher.xml;

import org.epzilla.util.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * This class is to modify the configuration settings in the XML file
 * Author: Chathura
 * Date: Jul 17, 2010
 * Time: 5:11:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationManager {
    String[] nodeIPs;
    String clusterID;
    String comp = "Dispatcher";
    static int count = 1;

    public ConfigurationManager(String[] IPs,String cID) {
        this.nodeIPs = IPs;
        this.clusterID = cID;
    }

    public boolean writeInfo() {
        boolean toReturn;
        toReturn = setEpZillaIpConfig();

        return toReturn;
    }



    private boolean setEpZillaIpConfig() {
        boolean toReturn = true;
        for (String nodes : nodeIPs) {
            boolean status = isValidIp(nodes);
            if (!status) {
                toReturn = false;
                break;
            }
        }
        if (toReturn) {
            writeEpzillaIpConfig(nodeIPs, clusterID, comp);
        }
        return toReturn;
    }

    /**
     * This method write data to epZilla IP config xml  
     * @param ips
     * @param cid
     * @param comp
     */
    private static void writeEpzillaIpConfig(String[] ips, String cid, String comp) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("EpzillaIpConfig.xml"));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.newLine();
            writer.write("<cluster id=\"" + cid + "\" component =\"" + comp + "\" />");
            writer.newLine();
            for (String ip : ips) {

                if (count==1) {
                    writer.write("\t" + "<ip uid=\"" + count + "\" default=\"" + "true" + "\">");
                    writer.write(ip);
                    writer.write("</ip>");
                    writer.newLine();
                    count++;
                } else {
                    writer.write("\t" + "<ip uid=\"" + count + "\">");
                    writer.write(ip);
                    writer.write("</ip>");
                    writer.newLine();
                    count++;
                }
            }

            writer.write("</cluster>");

            writer.flush();
            writer.close();
            writer = null;
        } catch (IOException e) {
            Logger.error("XML write error: ", e);
        }
    }

    /**
     * This method check the IP is valid, use regular expression to validate
     *
     * @param ip
     * @return
     */
    private static boolean isValidIp(final String ip) {
        boolean format = ip.matches("^[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}$");
        if (format) {
            boolean validIp = true;
            String[] values = ip.split("\\.");
            for (String value : values) {
                short v = Short.valueOf(value);
                if ((v < 0) || (v > 255)) {
                    validIp = false;
                    break;
                }
            }
            return validIp;
        }
        return false;
    }
}