package org.epzilla.clusterNode.xml;

import org.epzilla.util.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

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
    String[] accumulators;

    public ConfigurationManager(String[] IPs, String[] accumulators) {
        this.nodeIPs = IPs;
        this.accumulators = accumulators;
    }

    public boolean writeInfo() {
        boolean toReturn;
        toReturn = setAccumulators();
        toReturn = setEpZillaIpConfig();

        return toReturn;
    }

    /**
     * This method set Accumulator IP's
     * @return
     */
    private boolean setAccumulators() {
        boolean toReturn = true;
        for (String acc : accumulators) {
            boolean status = isValidIp(acc);
            if (!status) {
                toReturn = false;
                break;
            }
        }
        if (toReturn) {
            writeAccumulatorInfo(accumulators);
        }
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
            writeEpzillaIpConfig(nodeIPs);
        }
        return toReturn;
    }

    /**
     * This method write Accumulator data to XML file
     *
     * @param ips
     */
    private static void writeAccumulatorInfo(String[] ips) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Accumulators.xml"));

            writer.write("<accumulator>");
            writer.newLine();

            for (String ip : ips) {
                writer.write("\t" + "<accumulator IP=\"" + ip + "\"  />");
                writer.newLine();
            }

            writer.write("</accumulator>");

            writer.flush();
            writer.close();
            writer = null;
        } catch (IOException e) {
            Logger.error("XML write error: ", e);
        }
    }

    private static void writeEpzillaIpConfig(String[] ips) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("EpzillaIpConfig.xmll"));

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
