package com.fiilda.launcher

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * The launcher speaks Japanese when the device (or per-app) language is Japanese, and English
 * otherwise. The default locale is read on every call so a language change applies as soon as
 * the activity is recreated.
 */
internal fun isJapaneseUi(): Boolean = Locale.getDefault().language == Locale.JAPANESE.language

/** Check the language used by the launcher UI. */
internal fun isKoreanUi(): Boolean = Locale.getDefault().language == Locale.KOREAN.language

/** Picks the Japanese or English text for the current UI language. */
internal fun tr(ja: String, en: String): String = when {
    isJapaneseUi() -> ja
    isKoreanUi() -> translateKorean(en)
    else -> en
}

private val JapaneseWeekdayLetters = listOf("月", "火", "水", "木", "金", "土", "日")
private val EnglishShortDate = DateTimeFormatter.ofPattern("EEE, MMM d", Locale.ENGLISH)

/** Localized date label. */
internal fun fullDateLabel(date: LocalDate): String = when {
    isJapaneseUi() -> "${date.monthValue}月${date.dayOfMonth}日 ${weekdayLabel(date)}"
    isKoreanUi() -> "${date.monthValue}월 ${date.dayOfMonth}일 ${weekdayLabel(date)}"
    else -> date.format(EnglishShortDate)
}

/** Full weekday name. */
internal fun weekdayLabel(date: LocalDate): String = when {
    isJapaneseUi() -> "${shortWeekday(date.dayOfWeek)}曜日"
    isKoreanUi() -> listOf("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일")[date.dayOfWeek.value - 1]
    else -> date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
}

/** Short weekday name. */
internal fun shortWeekday(day: DayOfWeek): String = when {
    isJapaneseUi() -> JapaneseWeekdayLetters[day.value - 1]
    isKoreanUi() -> listOf("월", "화", "수", "목", "금", "토", "일")[day.value - 1]
    else -> day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
}

/** Calendar column header. */
internal fun weekdayInitial(day: DayOfWeek): String = when {
    isJapaneseUi() -> JapaneseWeekdayLetters[day.value - 1]
    isKoreanUi() -> listOf("월", "화", "수", "목", "금", "토", "일")[day.value - 1]
    else -> day.getDisplayName(TextStyle.NARROW, Locale.ENGLISH)
}

/** "September 2026". */
internal fun englishMonthYear(month: YearMonth): String =
    if (isKoreanUi()) "${month.year}년 ${month.monthValue}월"
    else "${month.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} ${month.year}"
