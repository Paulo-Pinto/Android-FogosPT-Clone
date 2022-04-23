package pt.ulusofona.deisi.cm2122.g21700980_21906966

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

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

//    fun goToDashboardFragment(fm: FragmentManager) {
//        placeFragment(fm, DashboardFragment())
//    }

    fun goToExtraFragment(fm: FragmentManager) {
        placeFragment(fm, ExtraFragment())
    }

    // TODO : criar fragmento risco

}