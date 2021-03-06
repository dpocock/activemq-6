/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.tests.unit.jms.misc;

import org.junit.Test;

import java.io.File;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.jms.Connection;
import javax.jms.ConnectionMetaData;

import org.junit.Assert;

import org.apache.activemq.core.server.ActiveMQServer;
import org.apache.activemq.core.server.ActiveMQServers;
import org.apache.activemq.jms.client.ActiveMQConnectionMetaData;
import org.apache.activemq.tests.unit.UnitTestLogger;
import org.apache.activemq.tests.util.UnitTestCase;

/**
 * @author <a href="ataylor@redhat.com">Andy Taylor</a>
 */
public class ManifestTest extends UnitTestCase
{
   // Constants -----------------------------------------------------

   // Static --------------------------------------------------------

   // Attributes ----------------------------------------------------

   // Constructors --------------------------------------------------

   // Public --------------------------------------------------------

   @Test
   public void testManifestEntries() throws Exception
   {
      Properties props = System.getProperties();
      String userDir = props.getProperty("build.lib");

      UnitTestLogger.LOGGER.trace("userDir is " + userDir);

      // The jar must be there
      File file = new File("build/jars", "activemq-core.jar");
      Assert.assertTrue(file.exists());

      // Open the jar and load MANIFEST.MF
      JarFile jar = new JarFile(file);
      Manifest manifest = jar.getManifest();

      // Open a connection and get ConnectionMetaData
      Connection conn = null;

      try
      {
         ActiveMQServer server = ActiveMQServers.newActiveMQServer(createBasicConfig());

         ConnectionMetaData meta = new ActiveMQConnectionMetaData(server.getVersion());

         // Compare the value from ConnectionMetaData and MANIFEST.MF
         Attributes attrs = manifest.getMainAttributes();

         Assert.assertEquals(meta.getProviderVersion(), attrs.getValue("ActiveMQ-Version"));
      }
      finally
      {
         if (conn != null)
         {
            conn.close();
         }
      }
   }

   // Package protected ---------------------------------------------

   // Protected -----------------------------------------------------
}
