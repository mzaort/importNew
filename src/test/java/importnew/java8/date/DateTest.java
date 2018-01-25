package importnew.java8.date;

import static org.junit.Assert.assertEquals;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import org.junit.Test;

/**
 * Your intuition is the opposite of the terminology used by both Joda-Time and
 * java.time.
 * 
 * Both frameworks have a class named LocalDate to represent a date-only value
 * without time-of-day and without time zone. The "local" means "could be any
 * locality, not any particular locality".
 * 
 * @author mzaort
 *
 */
public class DateTest {

	@Test
	public void testOffset() {
		LocalDateTime dateTime = LocalDateTime.now();
		ZoneId id = ZoneId.of("Europe/Paris");
		ZonedDateTime zoned = ZonedDateTime.of(dateTime, id);
		assertEquals(id, ZoneId.from(zoned));
		System.out.println(id);

		zoned = ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]");
		System.out.println(zoned);
		assertEquals(id, ZoneId.from(zoned));

		ZoneOffset offset = ZoneOffset.of("+02:00");
		OffsetTime time = OffsetTime.now();
		System.out.println(time);
		// changes offset, while keeping the same point on the timeline
		System.out.println(time.withOffsetSameInstant(offset));
		// changes the offset, and updates the point on the timeline
		OffsetTime changeTimeWithNewOffset = time.withOffsetSameLocal(offset);
		System.out.println(changeTimeWithNewOffset);
		// Can also create new object with altered fields as before
		System.out.println(changeTimeWithNewOffset.withHour(3).plusSeconds(2));

		// 3 years, 2 months, 1 day
		Period period = Period.of(3, 2, 1);
		LocalDate oldDate = LocalDate.now();
		ZonedDateTime oldDateTime = ZonedDateTime.now();

		// You can modify the values of dates using periods
		LocalDate newDate = oldDate.plus(period);
		System.out.println(newDate);
		ZonedDateTime newDateTime = oldDateTime.minus(period);
		System.out.println(newDateTime);
		// Components of a Period are represented by ChronoUnit values
		assertEquals(1, period.get(ChronoUnit.DAYS));

		// A duration of 3 seconds and 5 nanoseconds
		Duration duration = Duration.ofSeconds(3, 5);
		Duration oneDay = Duration.between(LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.DAYS));
		System.out.println(duration);
		System.out.println(oneDay);
	}

	@Test
	public void testWithAdjust() {
		LocalDateTime now = LocalDateTime.now();
		System.out.println(now.withDayOfMonth(12).withYear(2012));
		System.out.println(now.plusWeeks(12).minus(1, ChronoUnit.YEARS));
		System.out.println(now.toLocalDate());
		System.out.println(now.getMonth());
		System.out.println(now.getMonthValue());
		System.out.println(now.truncatedTo(ChronoUnit.SECONDS));
		System.out.println(now.with(TemporalAdjusters.lastDayOfMonth()));
		System.out.println(now.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY)));
		System.out.println(now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)));
		System.out.println(now.with(LocalTime.of(10, 12)));
	}

	@Test
	public void testDuration() {
		LocalDateTime from = LocalDateTime.of(2017, 10, 11, 1, 10, 21);
		LocalDateTime to = LocalDateTime.of(2018, 10, 1, 18, 2, 1);
		Duration duration = Duration.between(from, to);
		System.out.println(duration.toDays());
		System.out.println(duration.toHours());
	}

	@Test
	public void testZonedDateTime() {
		System.out.println(ZonedDateTime.now());
		System.out.println(ZonedDateTime.now(Clock.system(ZoneId.of("Europe/Paris"))));
	}

	// ZoneId Instant
	// LocalDate LocalTime LocalDateTime
	// Duration Clock
	@Test
	public void testDate() {
		System.out.println(LocalDate.now());
		System.out.println(LocalDate.of(2011, 8, 24));
		System.out.println(LocalDate.parse("2011-10-01"));

		System.out.println(LocalDate.now().plusDays(1));
		System.out.println(LocalDate.now().minus(1, ChronoUnit.MONTHS));

		System.out.println(LocalDate.now().getDayOfWeek());
		System.out.println(LocalDate.now().getDayOfMonth());
		System.out.println(LocalDate.now().getDayOfYear());

		System.out.println(LocalDate.now().isLeapYear());
		System.out.println(LocalDate.now().isBefore(LocalDate.of(2017, 12, 31)));
		System.out.println(LocalDate.now().isAfter(LocalDate.of(2018, 12, 31)));

		System.out.println(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));
		System.out.println(LocalDate.now().withDayOfMonth(1));

		LocalDate birthday = LocalDate.of(2017, 12, 23);
		MonthDay birth = MonthDay.of(12, 23);
		MonthDay birthMM = MonthDay.from(birthday);
		System.out.println(birth.equals(birthMM));

		LocalDate start = LocalDate.of(2017, 1, 13);
		LocalDate end = start.plus(Period.ofDays(4));
		System.out.println(start + " " + end);
		System.out.println(ChronoUnit.DAYS.between(start, end));
	}

	@Test
	public void testConvert() {
		System.out.println(new Date());
		System.out.println(new Date().toInstant());
		System.out.println(Date.from(Instant.now()));

		System.out.println(LocalDateTime.now().toInstant(ZoneOffset.UTC));
		System.out.println(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
		System.out.println(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC));
		System.out.println(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).toLocalDate());
		System.out.println(LocalTime.now().atDate(LocalDate.MIN).toInstant(ZoneOffset.UTC));

		System.out.println(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
		System.out.println(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
		System.out.println(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
	}

	@Test
	public void testDateFormat() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime now = LocalDateTime.now();
		System.out.println(now);
		System.out.println(format.format(now));

		TemporalAccessor temporal = format.parse("2017-12-23 12:56");
		System.out.println(LocalTime.from(temporal));
		System.out.println(LocalDate.from(temporal));
		System.out.println(LocalDateTime.from(temporal));
		System.out.println(LocalDateTime.now());
		System.out.println(LocalDateTime.from(temporal));
	}

}
