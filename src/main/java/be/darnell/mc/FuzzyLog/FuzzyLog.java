/*
 * Copyright (c) 2012 cedeel.
 * All rights reserved.
 * 
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * The name of the author may not be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package be.darnell.mc.FuzzyLog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author cedeel
 */
@SuppressWarnings("UnusedDeclaration")
public class FuzzyLog extends JavaPlugin {
  
  private static Map<String, LogFacility> logFacilities;
  
  @Override
  public void onLoad() {
    File logDir = new File("Logs");
    if (!logDir.exists()) {
      try {
        logDir.mkdirs();
      } catch (Exception e) {
        getLogger().log(Level.WARNING, "Could not create log file dir");
      }
    }
  }
  
  @Override
  public void onEnable() {
    PluginDescriptionFile pdf = this.getDescription();
    getLogger().log(Level.INFO, pdf.getName() + " starting up.");
    
    logFacilities = new HashMap<String, LogFacility>();
  }
  
  @Override
  public void onDisable() {
    for (LogFacility f : logFacilities.values()) {
      f.close();
    }
  }

  /**
   * Get a logging facility.
   *
   * @param name The name of the logging facility.
   * @return The logging facility in question.
   */
  public static LogFacility getFacility(String name) {
    return logFacilities.get(name);
  }

  /**
   * Adds a logging facility.
   *
   * @param name The name of the logging facility.
   * @return Whether the operation was successful.
   */
  public static boolean addFacility(String name) {
    if (!logFacilities.containsKey(name)) {
      logFacilities.put(name, new FileLogFacility(name, new File("Logs")));
      // logFacilities.put(name, new ChatLogFacility(name));
      return true;
    }
    // logger.info("addFacility(" + name + ") failed. Log facility already exists");
    return false;
  }

  /**
   * Removes a logging facility.
   *
   * @param name The name of the logging facility.
   * @return Whether the operation was successful.
   */
  public static boolean removeFacility(String name) {
    if (logFacilities.containsKey(name)) {
      logFacilities.get(name).close();
      logFacilities.remove(name);
      // logger.info("Removed log facility " + name);
      return true;
    }
    return false;
  }
}
