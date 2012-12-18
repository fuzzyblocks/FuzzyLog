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
package be.darnell.mc.HoeLogger.data;

import java.io.File;
import java.util.Calendar;

/**
 *
 * @author cedeel
 *
 */
public class FileDataStore implements DataStore {

  private static File _logfile;
  private static BufferedDataFileWriter bw;
  private static String _baseFile;
  // private static BufferedReader rdr;

  public FileDataStore(File logfile) {
    _baseFile = logfile.getPath();
    String today = String.format("%1$tY-%1$tm-%1$td", Calendar.getInstance());
    _logfile = new File(_baseFile + today + ".log");
    try {
      if (!_logfile.exists()) {
        if (!_logfile.getParentFile().exists()) {
          _logfile.getParentFile().mkdirs();
        }
        _logfile.createNewFile();
      }
      bw = new BufferedDataFileWriter(_logfile);
      // rdr = new BufferedReader(new FileReader(_logfile));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean writeSingle(String message) {
    try {
      updateFile(_baseFile);
      bw.write(message);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public void persist() throws RuntimeException {
    try {
      bw.close();
      // rdr.close();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
  
  private void updateFile(String f) {
    String today = String.format("%1$tY-%1$tm-%1$td", Calendar.getInstance());
    File file = new File(f + today + ".log");
    if(_logfile.equals(file)) return;
    
    bw.close();
    bw = new BufferedDataFileWriter(file);
  }

//  private void resetReader() {
//    try {
//      rdr = new BufferedReader(new FileReader(_logfile));
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
}
