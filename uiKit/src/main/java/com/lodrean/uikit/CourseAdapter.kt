package com.lodrean.uikit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lodrean.model.Course
import com.lodrean.uikit.databinding.ItemCourseCardBinding

class CourseAdapter(
    private val onFavoriteClick: (Course) -> Unit,
    private val onItemClick: (Course) -> Unit = {},
) : ListAdapter<Course, CourseAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCourseCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemCourseCardBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            binding.titleText.text = course.title
            binding.summaryText.text = course.summary
            Glide.with(binding.coverImage)
                .load(course.coverUrl)
                .placeholder(R.drawable.cover)
                .into(binding.coverImage)
            binding.favoriteButton.setImageResource(
                if (course.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
            binding.favoriteButton.setOnClickListener { onFavoriteClick(course) }
            binding.root.setOnClickListener { onItemClick(course) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean =
            oldItem == newItem
    }
}
