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

import java.io.*;

public class BufferedDataFileWriter
{
	private File dataFile;
	private String[] cache;
	private int index;
	private final int cacheSize = 15;

	public BufferedDataFileWriter (File dF)
	{
		dataFile = dF;
		cache = new String[cacheSize];
		index = 0;
	}

	public void write (String s)
	{
		if (index >= cache.length)
			flush();
		cache[index] = s;
		index++;
//		System.out.println("Wrote: " + s);
	}

	/**
   * Flushes the data in cache to disk.
   */
  private void flush ()
	{
		if (index > 0)
		{
			try
			{
				PrintWriter pw = new PrintWriter(new FileWriter(dataFile, true));
				for (int i = 0; ((i < index) && (i < cache.length)); i++)
				{
					pw.println(cache[i]);
				}
//				System.out.println("Log: Flushed buffer.");
				cache = new String[cacheSize];
				index = 0;
				pw.close();
			}
			catch (IOException e)
			{
				throw new RuntimeException ("Log: Error flushing buffer: " + e);
			}
		}
	}

	/**
   * Erases the file.
   */
  private void erase ()
	{
		flush();
		try
		{
			PrintWriter pw = new PrintWriter(new FileWriter(dataFile, false));
			pw.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException ("Log: Error erasing data: " + e);
		}
	}

	public void close ()
	{
		flush();
	}
}