package pt.ulusofona.deisi.cm2122.g21700980_21906966

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import pt.ulusofona.deisi.cm2122.g21700980_21906966.dashboard.DashboardFragment
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireDetailFragment
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI
import pt.ulusofona.deisi.cm2122.g21700980_21906966.list.FireListFragment
import pt.ulusofona.deisi.cm2122.g21700980_21906966.map.MapFragment
import pt.ulusofona.deisi.cm2122.g21700980_21906966.register.RegisterFireFragment

object NavigationManager {

    private fun placeFragment(fm: FragmentManager, fragment: Fragment) {
        val transition = fm.beginTransaction()
        transition.replace(R.id.frame, fragment)
        transition.addToBackStack(null)
        transition.commit()
    }

    fun goToRegisterFireFragment(fm: FragmentManager) {
        placeFragment(fm, RegisterFireFragment())
    }

    fun goToFireListFragment(fm: FragmentManager) {
        placeFragment(fm, FireListFragment())
    }

    fun goToFireDetailFragment(fm: FragmentManager, fireui: FireUI) {
        placeFragment(fm, FireDetailFragment.newInstance(fireui))
    }

    fun goToDashboardFragment(fm: FragmentManager) {
        placeFragment(fm, DashboardFragment())
    }

    fun goToMapFragment(fm: FragmentManager) {
        placeFragment(fm, MapFragment())
    }

    fun goToExtraFragment(fm: FragmentManager) {
        placeFragment(fm, ExtraFragment())
    }

    // TODO : criar fragmento risco

}