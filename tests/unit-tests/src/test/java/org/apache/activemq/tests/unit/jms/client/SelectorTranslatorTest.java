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
package org.apache.activemq.tests.unit.jms.client;

import org.junit.Test;

import org.junit.Assert;

import org.apache.activemq.jms.client.SelectorTranslator;
import org.apache.activemq.tests.util.UnitTestCase;

/**
 *
 * A SelectorTranslatorTest
 *
 * @author <a href="mailto:tim.fox@jboss.com">Tim Fox</a>
 *
 */
public class SelectorTranslatorTest extends UnitTestCase
{
   @Test
   public void testParseNull()
   {
      Assert.assertNull(SelectorTranslator.convertToActiveMQFilterString(null));
   }

   @Test
   public void testParseSimple()
   {
      final String selector = "color = 'red'";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));
   }

   @Test
   public void testParseMoreComplex()
   {
      final String selector = "color = 'red' OR cheese = 'stilton' OR (age = 3 AND shoesize = 12)";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));
   }

   @Test
   public void testParseJMSDeliveryMode()
   {
      String selector = "JMSDeliveryMode='NON_PERSISTENT'";

      Assert.assertEquals("HQDurable='NON_DURABLE'", SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = "JMSDeliveryMode='PERSISTENT'";

      Assert.assertEquals("HQDurable='DURABLE'", SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = "color = 'red' AND 'NON_PERSISTENT' = JMSDeliveryMode";

      Assert.assertEquals("color = 'red' AND 'NON_DURABLE' = HQDurable",
                          SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = "color = 'red' AND 'PERSISTENT' = JMSDeliveryMode";

      Assert.assertEquals("color = 'red' AND 'DURABLE' = HQDurable",
                          SelectorTranslator.convertToActiveMQFilterString(selector));

      checkNoSubstitute("JMSDeliveryMode");
   }

   @Test
   public void testParseJMSPriority()
   {
      String selector = "JMSPriority=5";

      Assert.assertEquals("HQPriority=5", SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " JMSPriority = 7";

      Assert.assertEquals(" HQPriority = 7", SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " JMSPriority = 7 OR 1 = JMSPriority AND (JMSPriority= 1 + 4)";

      Assert.assertEquals(" HQPriority = 7 OR 1 = HQPriority AND (HQPriority= 1 + 4)",
                          SelectorTranslator.convertToActiveMQFilterString(selector));

      checkNoSubstitute("JMSPriority");

      selector = "animal = 'lion' JMSPriority = 321 OR animal_name = 'xyzJMSPriorityxyz'";

      Assert.assertEquals("animal = 'lion' HQPriority = 321 OR animal_name = 'xyzJMSPriorityxyz'",
                          SelectorTranslator.convertToActiveMQFilterString(selector));

   }

   @Test
   public void testParseJMSMessageID()
   {
      String selector = "JMSMessageID='ID:HQ-12435678";

      Assert.assertEquals("HQUserID='ID:HQ-12435678", SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " JMSMessageID='ID:HQ-12435678";

      Assert.assertEquals(" HQUserID='ID:HQ-12435678", SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " JMSMessageID = 'ID:HQ-12435678";

      Assert.assertEquals(" HQUserID = 'ID:HQ-12435678", SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " myHeader = JMSMessageID";

      Assert.assertEquals(" myHeader = HQUserID", SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " myHeader = JMSMessageID OR (JMSMessageID = 'ID-HQ' + '12345')";

      Assert.assertEquals(" myHeader = HQUserID OR (HQUserID = 'ID-HQ' + '12345')", SelectorTranslator.convertToActiveMQFilterString(selector));

      checkNoSubstitute("JMSMessageID");
   }

   @Test
   public void testParseJMSTimestamp()
   {
      String selector = "JMSTimestamp=12345678";

      Assert.assertEquals("HQTimestamp=12345678", SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " JMSTimestamp=12345678";

      Assert.assertEquals(" HQTimestamp=12345678", SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " JMSTimestamp=12345678 OR 78766 = JMSTimestamp AND (JMSTimestamp= 1 + 4878787)";

      Assert.assertEquals(" HQTimestamp=12345678 OR 78766 = HQTimestamp AND (HQTimestamp= 1 + 4878787)",
                          SelectorTranslator.convertToActiveMQFilterString(selector));

      checkNoSubstitute("JMSTimestamp");

      selector = "animal = 'lion' JMSTimestamp = 321 OR animal_name = 'xyzJMSTimestampxyz'";

      Assert.assertEquals("animal = 'lion' HQTimestamp = 321 OR animal_name = 'xyzJMSTimestampxyz'",
                          SelectorTranslator.convertToActiveMQFilterString(selector));

   }

   @Test
   public void testParseJMSExpiration()
   {
      String selector = "JMSExpiration=12345678";

      Assert.assertEquals("HQExpiration=12345678", SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " JMSExpiration=12345678";

      Assert.assertEquals(" HQExpiration=12345678", SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " JMSExpiration=12345678 OR 78766 = JMSExpiration AND (JMSExpiration= 1 + 4878787)";

      Assert.assertEquals(" HQExpiration=12345678 OR 78766 = HQExpiration AND (HQExpiration= 1 + 4878787)",
                          SelectorTranslator.convertToActiveMQFilterString(selector));

      checkNoSubstitute("JMSExpiration");

      selector = "animal = 'lion' JMSExpiration = 321 OR animal_name = 'xyzJMSExpirationxyz'";

      Assert.assertEquals("animal = 'lion' HQExpiration = 321 OR animal_name = 'xyzJMSExpirationxyz'",
                          SelectorTranslator.convertToActiveMQFilterString(selector));

   }

   @Test
   public void testParseJMSCorrelationID()
   {
      String selector = "JMSCorrelationID='ID:HQ-12435678";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " JMSCorrelationID='ID:HQ-12435678";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " JMSCorrelationID = 'ID:HQ-12435678";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " myHeader = JMSCorrelationID";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " myHeader = JMSCorrelationID OR (JMSCorrelationID = 'ID-HQ' + '12345')";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      checkNoSubstitute("JMSCorrelationID");
   }

   @Test
   public void testParseJMSType()
   {
      String selector = "JMSType='aardvark'";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " JMSType='aardvark'";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " JMSType = 'aardvark'";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " myHeader = JMSType";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = " myHeader = JMSType OR (JMSType = 'aardvark' + 'sandwich')";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      checkNoSubstitute("JMSType");
   }



   // Private -------------------------------------------------------------------------------------

   private void checkNoSubstitute(final String fieldName)
   {
      String selector = "Other" + fieldName + " = 767868";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = "cheese = 'cheddar' AND Wrong" + fieldName + " = 54";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = "fruit = 'pomegranate' AND " + fieldName + "NotThisOne = 'tuesday'";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = "animal = 'lion' AND animal_name = '" + fieldName + "'";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = "animal = 'lion' AND animal_name = ' " + fieldName + "'";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = "animal = 'lion' AND animal_name = ' " + fieldName + " '";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = "animal = 'lion' AND animal_name = 'xyz " + fieldName + "'";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = "animal = 'lion' AND animal_name = 'xyz" + fieldName + "'";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = "animal = 'lion' AND animal_name = '" + fieldName + "xyz'";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));

      selector = "animal = 'lion' AND animal_name = 'xyz" + fieldName + "xyz'";

      Assert.assertEquals(selector, SelectorTranslator.convertToActiveMQFilterString(selector));
   }

}
