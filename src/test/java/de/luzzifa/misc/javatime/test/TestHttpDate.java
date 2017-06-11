/*
 * Copyright 2017 Wolfgang Deifel, jforge.net, luzzifa.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */

package de.luzzifa.misc.javatime.test;

import de.luzzifa.misc.javatime.HttpDate;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * purpose of this class?
 *
 * @author wolle_d
 */
@RunWith(JUnitParamsRunner.class)
public class TestHttpDate
{
  @Test
  @Parameters
  public void testDateFormatCanBeParsed(String dateString, long expectedSeconds)
  {
    long actualSeconds = HttpDate.parseLong(dateString);

    Assert.assertEquals(expectedSeconds, actualSeconds);
  }

  private Object parametersForTestDateFormatCanBeParsed()
  {
    final String dateFormatRfc822 = "Sa, 10 Jun 2017 14:59:24 MESZ";
    final String dateFormatRfc850 = "Samstag, 10-Jun-2017 14:59:24 MESZ";
    final String dateFormatAsctime = "Sa Jun 10 14:59:24 2017";
    final long secondsForDates = 1497099564;

    return $(
        $(dateFormatRfc822, secondsForDates),
        $(dateFormatRfc850, secondsForDates),
        $(dateFormatAsctime, secondsForDates)
    );
  }

  public static Object[] $(Object... params)
  {
    return params;
  }

}
