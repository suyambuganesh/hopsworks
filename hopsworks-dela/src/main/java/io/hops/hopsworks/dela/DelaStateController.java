/*
 * Copyright (C) 2013 - 2018, Logical Clocks AB and RISE SICS AB. All rights reserved
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS  OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR  OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package io.hops.hopsworks.dela;

import io.hops.hopsworks.common.util.Settings;
import io.hops.hopsworks.dela.exception.ThirdPartyException;
import java.security.KeyStore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.core.Response;

@Startup
@Singleton
public class DelaStateController {

  private final static Logger LOG = Logger.getLogger(DelaStateController.class.getName());

  @EJB
  private Settings settings;

  private boolean delaEnabled = false;
  private boolean delaCertsAvailable = false;
  private boolean transferDelaAvailable = false;
  private boolean hopssiteAvailable = false;
  
  private KeyStore keystore;
  private KeyStore truststore;
  private String keystorePassword;
      
  @PostConstruct
  private void init() {
    if (settings.isDelaEnabled()) {
      delaEnabled = settings.isDelaEnabled();
      LOG.log(Level.INFO, "dela enabled");
    } else {
      LOG.log(Level.INFO, "dela disabled");
    }
  }

  public boolean delaEnabled() {
    return delaEnabled;
  }
  
  public boolean delaAvailable() {
    return hopsworksDelaSetup() && transferDelaAvailable && hopssiteAvailable;
  }

  public void checkDelaAvailable() throws ThirdPartyException {
    if (!delaAvailable()) {
      throw new ThirdPartyException(Response.Status.BAD_REQUEST.getStatusCode(), "dela not available",
        ThirdPartyException.Source.LOCAL, "bad request");
    }
  }

  public boolean transferDelaAvailable() {
    return hopsworksDelaSetup() && transferDelaAvailable;
  }

  public boolean hopssiteAvailable() {
    return hopsworksDelaSetup() && hopssiteAvailable;
  }
  
  public void checkHopssiteAvailable() throws ThirdPartyException {
    if (!hopssiteAvailable()) {
      throw new ThirdPartyException(Response.Status.BAD_REQUEST.getStatusCode(), "hopssite not available", 
        ThirdPartyException.Source.LOCAL, "bad request");
    }
  }

  public boolean hopsworksDelaSetup() {
    return delaEnabled && delaCertsAvailable;
  }

  public void checkHopsworksDelaSetup() throws ThirdPartyException {
    if (!hopsworksDelaSetup()) {
      throw new ThirdPartyException(Response.Status.BAD_REQUEST.getStatusCode(), "remote dela not available",
        ThirdPartyException.Source.LOCAL, "bad request");
    }
  }

  public void transferDelaContacted() {
    transferDelaAvailable = true;
  }

  public void hopssiteContacted() {
    hopssiteAvailable = true;
  }
  
  public void hopssiteCertsAvailable(KeyStore keystore, KeyStore truststore, String keystorePassword) {
    delaCertsAvailable = true;
    this.keystore = keystore;
    this.truststore = truststore;
    this.keystorePassword = keystorePassword;
  }

  public KeyStore getKeystore() {
    return keystore;
  }

  public KeyStore getTruststore() {
    return truststore;
  }

  public String getKeystorePassword() {
    return keystorePassword;
  }
}
