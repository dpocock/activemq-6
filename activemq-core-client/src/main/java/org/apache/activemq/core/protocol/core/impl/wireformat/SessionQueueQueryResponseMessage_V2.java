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
package org.apache.activemq.core.protocol.core.impl.wireformat;

import org.apache.activemq.api.core.ActiveMQBuffer;
import org.apache.activemq.api.core.SimpleString;
import org.apache.activemq.api.core.client.ClientSession;
import org.apache.activemq.core.client.impl.QueueQueryImpl;
import org.apache.activemq.core.server.QueueQueryResult;

/**
 * @author Justin Bertram
 *
 */
public class SessionQueueQueryResponseMessage_V2 extends SessionQueueQueryResponseMessage
{
   private boolean autoCreationEnabled;

   public SessionQueueQueryResponseMessage_V2(final QueueQueryResult result)
   {
      this(result.getName(), result.getAddress(), result.isDurable(), result.isTemporary(),
           result.getFilterString(), result.getConsumerCount(), result.getMessageCount(), result.isExists(), result.isAutoCreateJmsQueues());
   }

   public SessionQueueQueryResponseMessage_V2()
   {
      this(null, null, false, false, null, 0, 0, false, false);
   }

   private SessionQueueQueryResponseMessage_V2(final SimpleString name,
                                               final SimpleString address,
                                               final boolean durable,
                                               final boolean temporary,
                                               final SimpleString filterString,
                                               final int consumerCount,
                                               final long messageCount,
                                               final boolean exists,
                                               final boolean autoCreationEnabled)
   {
      super(SESS_QUEUEQUERY_RESP_V2);

      this.durable = durable;

      this.temporary = temporary;

      this.consumerCount = consumerCount;

      this.messageCount = messageCount;

      this.filterString = filterString;

      this.address = address;

      this.name = name;

      this.exists = exists;

      this.autoCreationEnabled = autoCreationEnabled;
   }

   public boolean isAutoCreationEnabled()
   {
      return autoCreationEnabled;
   }

   @Override
   public void encodeRest(final ActiveMQBuffer buffer)
   {
      super.encodeRest(buffer);
      buffer.writeBoolean(autoCreationEnabled);
   }

   @Override
   public void decodeRest(final ActiveMQBuffer buffer)
   {
      super.decodeRest(buffer);
      autoCreationEnabled = buffer.readBoolean();
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + (autoCreationEnabled ? 1231 : 1237);
      return result;
   }

   public ClientSession.QueueQuery toQueueQuery()
   {
      return new QueueQueryImpl(isDurable(),
                                isTemporary(),
                                getConsumerCount(),
                                getMessageCount(),
                                getFilterString(),
                                getAddress(),
                                getName(),
                                isExists(),
                                isAutoCreationEnabled());
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof SessionQueueQueryResponseMessage_V2))
         return false;
      SessionQueueQueryResponseMessage_V2 other = (SessionQueueQueryResponseMessage_V2)obj;
      if (autoCreationEnabled != other.autoCreationEnabled)
         return false;
      return true;
   }
}
