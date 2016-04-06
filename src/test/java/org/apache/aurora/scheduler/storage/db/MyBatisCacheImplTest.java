/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.aurora.scheduler.storage.db;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MyBatisCacheImplTest {
  private MyBatisCacheImpl cache;

  @Before
  public void setUp() {
    cache = new MyBatisCacheImpl("cache.id");
  }

  @Test(expected = NullPointerException.class)
  public void testExceptionWithoutSize() {
    cache.getSize();
  }

  @Test
  public void testGetAndSet() {
    String key = "key";
    String value = "value";

    cache.setSize(100);

    assertNull(cache.getObject(key));

    cache.putObject(key, value);

    assertEquals(cache.getObject(key), value);

    cache.clear();

    assertNull(cache.getObject(key));
  }
}
