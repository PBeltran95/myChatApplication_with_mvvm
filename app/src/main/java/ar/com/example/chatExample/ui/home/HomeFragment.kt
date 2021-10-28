package ar.com.example.chatExample.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ar.com.example.chatExample.R
import ar.com.example.chatExample.core.Response
import ar.com.example.chatExample.data.models.User
import ar.com.example.chatExample.databinding.FragmentHomeBinding
import ar.com.example.chatExample.presentation.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), HomeAdapter.OnItemClickListener {

    private lateinit var binding : FragmentHomeBinding
    private val homeAdapter : HomeAdapter by lazy { HomeAdapter(requireContext(), this@HomeFragment) }
    private val viewModel : AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        recoverUsers()
    }

    private fun recoverUsers() {
        viewModel.getAllUsers().observe(viewLifecycleOwner, Observer {
            when(it){
                is Response.Loading -> {}
                is Response.Success -> {
                    initRecyclerView(it.data)
                }
                is Response.Failure -> {}
            }
        })
    }

    private fun initRecyclerView(response: MutableList<User>) {
        binding.rvHome.adapter = homeAdapter
        homeAdapter.setData(response)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.log_out_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.log_out_icon -> {viewModel.logOut()
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onUserClick(user: User) {
        val action = HomeFragmentDirections.actionHomeFragmentToChatFragment(user.userId, user.userName, user.userToken)
        findNavController().navigate(action)
    }
}