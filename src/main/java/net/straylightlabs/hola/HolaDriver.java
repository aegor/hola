/*
 * The MIT License
 *
 * Copyright (c) 2015-2018 Todd Kulesza <todd@dropline.net>
 *
 * This file is part of Hola.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.straylightlabs.hola;

import net.straylightlabs.hola.dns.Domain;
import net.straylightlabs.hola.sd.Instance;
import net.straylightlabs.hola.sd.Query;
import net.straylightlabs.hola.sd.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;

/**
 * A minimal implementation of mDNS-SD, as described in RFCs 6762 &amp; 6763.
 */

public class HolaDriver {
    final static Logger logger = LoggerFactory.getLogger(HolaDriver.class);

    public static void main(String[] args) {
        try {
            Service service = Service.fromName("_ortho._udp");
            byte[] ipAddress = new byte[4];
            ipAddress[0] = (byte) 192; ipAddress[1] = (byte) 168; ipAddress[2] = (byte) 31; ipAddress[3] = (byte) 56;
            InetAddress addr = Inet4Address.getByAddress(ipAddress);

            Query query = Query.createFor(service, Domain.LOCAL);
            Set<Instance> instances = query.runOnceOn(addr);
            instances.stream().forEach(System.out::println);
            if (instances.size() == 0) {
                logger.error("No instances of type {} found :(", service);
            }
        } catch (UnknownHostException e) {
            logger.error("Unknown host: ", e);
        } catch (IOException e) {
            logger.error("IO error: ", e);
        }
    }
}
