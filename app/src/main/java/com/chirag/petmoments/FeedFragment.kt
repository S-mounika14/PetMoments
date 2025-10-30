package com.chirag.petmoments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chirag.petmoments.databinding.FragmentFeedBinding
import com.google.android.material.chip.Chip

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostViewModel by activityViewModels()
    private lateinit var postAdapter: PostAdapter

    private val categories = listOf("All", "Dog", "Cat", "Bird", "Rabbit", "Other")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupCategoryChips()
        observePosts()
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter { post ->
            viewModel.toggleLike(post)
        }

        binding.recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupCategoryChips() {
        categories.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                setOnClickListener {
                    viewModel.allPosts.value?.let { posts ->
                        viewModel.filterByCategory(category, posts)
                    }
                }
            }
            binding.chipGroupCategories.addView(chip)
        }

        (binding.chipGroupCategories.getChildAt(0) as? Chip)?.isChecked = true
    }

    private fun observePosts() {
        viewModel.allPosts.observe(viewLifecycleOwner) { posts ->
            if (posts.isEmpty()) {
                binding.emptyStateLayout.visibility = View.VISIBLE
                binding.recyclerViewPosts.visibility = View.GONE
            } else {
                binding.emptyStateLayout.visibility = View.GONE
                binding.recyclerViewPosts.visibility = View.VISIBLE

                val category = viewModel.selectedCategory.value ?: "All"
                viewModel.filterByCategory(category, posts)
            }
        }

        viewModel.filteredPosts.observe(viewLifecycleOwner) { posts ->
            postAdapter.submitList(posts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}