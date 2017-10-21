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

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;

/**
 * run like static main()
 *
 * @author wolle_d
 */
public class EvaluateManual
{
  // for pattern see https://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.3.1
  public static final DateTimeFormatter PATTERN_RFC_1123 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz");
  public static final DateTimeFormatter PATTERN_RFC_1036 = DateTimeFormatter.ofPattern("EEEE, dd-MMM-yyyy HH:mm:ss zzz");
  public static final DateTimeFormatter PATTERN_ASC_TIME = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy");
  public static final List<DateTimeFormatter> HTTP_DATE_PATTERN = Arrays.asList(PATTERN_RFC_1123, PATTERN_RFC_1036, PATTERN_ASC_TIME);
  public static ZoneOffset systemDefaultOffset;

  @Test
  public void runMain()
  {
    ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
    systemDefaultOffset = ZoneOffset.from(now);

    String dateStringRfc1123 = PATTERN_RFC_1123.format(now);
    String dateStringRfc1036 = PATTERN_RFC_1036.format(now);
    String dateStringAscTime = PATTERN_ASC_TIME.format(now);

    System.out.println(dateStringRfc1123);
    checkDate(dateStringAscTime, now);

    System.out.println(dateStringRfc1036);
    checkDate(dateStringRfc1036, now);

    System.out.println(dateStringAscTime);
    checkDate(dateStringRfc1123, now);
  }

  private static void checkDate(String dateTimeString, ZonedDateTime comp)
  {
    DateTimeParseException ex = null;
    TemporalAccessor ta = null;
    for (DateTimeFormatter formatter : HTTP_DATE_PATTERN)
    {
      try
      {
        ta = formatter.parse(dateTimeString);
      }
      catch (DateTimeParseException e)
      {
        if (ex==null)
        {
          // remember only first exception
          ex = e;
        }
      }
    }
    if (ta==null)
    {
      System.out.println("BAD parse failed due "+ex);
      throw ex;
    }
    System.out.println("Parsed successfully");
    long epochSeconds;
    if (ta.isSupported(ChronoField.OFFSET_SECONDS))
    {
      epochSeconds = ZonedDateTime.from(ta).toEpochSecond();
    }
    else
    {
      epochSeconds = LocalDateTime.from(ta).toEpochSecond(systemDefaultOffset);
    }
    if (comp.toEpochSecond() == epochSeconds)
    {
      System.out.println("OK");
    }
    else
    {
      System.out.println("BAD");
    }
  }
}
