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

package de.luzzifa.misc.javatime;

import org.jetbrains.annotations.NotNull;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * HttpDate is autility to parse and format dates used in http headers according to rfc2616 sec 3.3.1
 * see https://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.3.1
 *
 * @author wolle_d
 */
public class HttpDate
{
  // Http Date format as specified by RFC 822, updated by RFC 1123
  public static final DateTimeFormatter PATTERN_RFC_822 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz");

  // Http Date format as specified by RFC 850, obsolete by RFC 1036
  public static final DateTimeFormatter PATTERN_RFC_850 = DateTimeFormatter.ofPattern("EEEE, dd-MMM-yyyy HH:mm:ss zzz");

  // Http Date format ANSI C's asctime() format
  public static final DateTimeFormatter PATTERN_ASC_TIME = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy");

  public static final List<DateTimeFormatter> HTTP_DATE_PATTERN =
      Arrays.asList(PATTERN_RFC_822, PATTERN_RFC_850, PATTERN_ASC_TIME);

  public static ZoneOffset SYSTEM_DEFAULT_OFFSET = ZoneOffset.from(ZonedDateTime.now(ZoneId.systemDefault()));

  @NotNull
  public static TemporalAccessor parse(CharSequence dateTimeString)
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
      throw ex;
    }
    return ta;
  }

  public static Instant parseInstant(CharSequence string)
  {
    final TemporalAccessor ta = parse(string);
    if (ta.isSupported(ChronoField.OFFSET_SECONDS))
    {
      return Instant.from(ta);
    }
    return LocalDateTime.from(ta).toInstant(SYSTEM_DEFAULT_OFFSET);
  }

  public static long parseLong(CharSequence dateTimeString)
  {
    return parseInstant(dateTimeString).getEpochSecond();
  }

  public static <T extends TemporalAccessor> T parseQuery(CharSequence string, Function<TemporalAccessor, T> query)
  {
    final TemporalAccessor ta = parse(string);
    return query.apply(ta);
  }

  public static String format(TemporalAccessor ta)
  {
    return PATTERN_RFC_822.format(ta);
  }

}
