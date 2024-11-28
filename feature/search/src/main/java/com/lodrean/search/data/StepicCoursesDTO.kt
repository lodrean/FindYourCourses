package com.lodrean.search.data

data class StepicCoursesDTO(
    val courses: List<Course>,
    val enrollments: List<Any>,
    val meta: Meta,
)

data class Course(
    val acquired_assets: List<Any>,
    val acquired_skills: List<Any>,
    val actions: Actions,
    val admins_group: Any,
    val announcements: List<Any>,
    val assistants_group: Any,
    val authors: List<Int>,
    val became_paid_at: Any,
    val became_published_at: String,
    val begin_date: Any,
    val begin_date_source: Any,
    val canonical_url: String,
    val certificate: String,
    val certificate_cover_org: Any,
    val certificate_distinction_link: Any,
    val certificate_distinction_threshold: Int,
    val certificate_footer: Any,
    val certificate_link: Any,
    val certificate_regular_link: Any,
    val certificate_regular_threshold: Int,
    val certificates_count: Int,
    val challenges_count: Int,
    val child_courses: List<Any>,
    val child_courses_count: Int,
    val commission_basic: Any,
    val commission_promo: Any,
    val content_details: List<Any>,
    val continue_url: String,
    val course_format: String,
    val course_type: String,
    val cover: String,
    val create_date: String,
    val currency_code: Any,
    val default_promo_code_discount: Any,
    val default_promo_code_expire_date: Any,
    val default_promo_code_is_percent_discount: Any,
    val default_promo_code_name: Any,
    val default_promo_code_price: Any,
    val description: String,
    val difficulty: Any,
    val discussion_proxy: Any,
    val discussion_threads: List<Any>,
    val discussions_count: Int,
    val display_price: String,
    val end_date: Any,
    val end_date_source: Any,
    val enrollment: Any,
    val first_deadline: Any,
    val first_lesson: Int,
    val first_unit: Int,
    val grading_policy: String,
    val grading_policy_source: String,
    val hard_deadline: Any,
    val hard_deadline_source: Any,
    val has_tutors: Boolean,
    val id: Int,
    val instructor_reviews_count: Int,
    val instructors: List<Any>,
    val intro: String,
    val intro_video: IntroVideo,
    val is_active: Boolean,
    val is_adaptive: Boolean,
    val is_archived: Boolean,
    val is_censored: Boolean,
    val is_certificate_auto_issued: Boolean,
    val is_certificate_issued: Boolean,
    val is_certificate_with_score: Boolean,
    val is_contest: Boolean,
    val is_enabled: Boolean,
    val is_favorite: Boolean,
    val is_featured: Boolean,
    val is_idea_compatible: Boolean,
    val is_in_wishlist: Boolean,
    val is_paid: Boolean,
    val is_popular: Boolean,
    val is_processed_with_paddle: Boolean,
    val is_proctored: Boolean,
    val is_public: Boolean,
    val is_self_paced: Boolean,
    val is_unsuitable: Boolean,
    val issue: Any,
    val language: String,
    val last_deadline: Any,
    val last_step: String,
    val last_update_price_date: Any,
    val learners_count: Int,
    val learners_group: Any,
    val learning_format: String,
    val lessons_count: Int,
    val lti_consumer_key: String,
    val lti_private_profile: Boolean,
    val lti_secret_key: String,
    val moderators_group: Any,
    val options: Options,
    val owner: Int,
    val parent_courses: List<Any>,
    val peer_reviews_count: Int,
    val position: Int,
    val possible_currencies: List<Any>,
    val possible_type: Any,
    val preview_lesson: Any,
    val preview_unit: Any,
    val price: Any,
    val price_tier: Any,
    val proctor_url: Any,
    val progress: Any,
    val quizzes_count: Int,
    val readiness: Double,
    val referral_link: Any,
    val requirements: String,
    val review_summary: Int,
    val schedule_link: Any,
    val schedule_long_link: Any,
    val schedule_type: String,
    val sections: List<Int>,
    val slug: String,
    val social_providers: List<Any>,
    val soft_deadline: Any,
    val soft_deadline_source: Any,
    val subscriptions: List<String>,
    val summary: String,
    val tags: List<Int>,
    val target_audience: String,
    val teachers_group: Any,
    val testers_group: Any,
    val time_to_complete: Int,
    val title: String,
    val title_en: String,
    val total_units: Int,
    val update_date: String,
    val user_certificate: Any,
    val videos_duration: Int,
    val with_certificate: Boolean,
    val workload: String,
)

data class Meta(
    val has_next: Boolean,
    val has_previous: Boolean,
    val page: Int,
)

data class Actions(
    val can_be_bought: CanBeBought,
    val can_be_deleted: CanBeDeleted,
    val can_be_price_changed: CanBePriceChanged,
    val edit_advanced_settings: EditAdvancedSettings,
    val edit_lti: EditLti,
    val edit_reports: EditReports,
    val edit_tags: EditTags,
    val manage_permissions: ManagePermissions,
    val view_grade_book: ViewGradeBook,
    val view_grade_book_page: ViewGradeBookPage,
    val view_reports: ViewReports,
    val view_revenue: ViewRevenue,
)

data class IntroVideo(
    val duration: Int,
    val filename: String,
    val id: Int,
    val status: String,
    val thumbnail: String,
    val upload_date: String,
    val urls: List<Url>,
)

class Options

data class CanBeBought(
    val enabled: Boolean,
)

data class CanBeDeleted(
    val enabled: Boolean,
)

data class CanBePriceChanged(
    val enabled: Boolean,
)

data class EditAdvancedSettings(
    val enabled: Boolean,
    val needs_permission: String,
)

data class EditLti(
    val enabled: Boolean,
    val needs_permission: String,
)

data class EditReports(
    val enabled: Boolean,
    val needs_permission: String,
)

data class EditTags(
    val enabled: Boolean,
)

data class ManagePermissions(
    val enabled: Boolean,
    val needs_permission: String,
)

data class ViewGradeBook(
    val enabled: Boolean,
    val needs_permission: String,
)

data class ViewGradeBookPage(
    val enabled: Boolean,
    val needs_permission: String,
)

data class ViewReports(
    val enabled: Boolean,
    val needs_permission: String,
)

data class ViewRevenue(
    val enabled: Boolean,
)

data class Url(
    val quality: String,
    val url: String,
)
