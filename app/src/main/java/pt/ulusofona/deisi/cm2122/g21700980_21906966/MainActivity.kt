package pt.ulusofona.deisi.cm2122.g21700980_21906966

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.ActivityMainBinding
import android.Manifest

import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import pt.ulusofona.deisi.cm2122.g21700980_21906966.connectivity.RetrofitBuilder
import pt.ulusofona.deisi.cm2122.g21700980_21906966.management.FireDatabase
import pt.ulusofona.deisi.cm2122.g21700980_21906966.management.FogosRepository
import pt.ulusofona.deisi.cm2122.g21700980_21906966.management.FogosRetrofit
import pt.ulusofona.deisi.cm2122.g21700980_21906966.management.FogosRoom
import pt.ulusofona.deisi.cm2122.g21700980_21906966.map.FusedLocation

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FusedLocation.start(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionsBuilder(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION).build().send { result ->
            if (result.allGranted()) {
                if (!screenRotated(savedInstanceState)) {
                    NavigationManager.goToDashboardFragment(supportFragmentManager)
                }
            } else {
                finish()
            }
        }

        FogosRepository.init(this,
            FogosRoom(FireDatabase.getInstance(this).fogosDao()),
            FogosRetrofit(RetrofitBuilder.getInstance("https://api.fogos.pt/"))
        )
    }

    private fun screenRotated(savedInstanceState: Bundle?): Boolean {
        return savedInstanceState != null
    }

    override fun onStart() {
        super.onStart()
        setSupportActionBar(binding.toolbar)
        setupDrawerMenu()
    }

    private fun setupDrawerMenu() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawer, binding.toolbar,
            R.string.drawer_open, R.string.drawer_closed
        )
        binding.navDrawer.setNavigationItemSelectedListener {
            onClickNavigationItem(it)
        }
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun onClickNavigationItem(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_fogospt -> NavigationManager.goToRegisterFireFragment(supportFragmentManager)
            R.id.nav_firelist -> NavigationManager.goToFireListFragment(supportFragmentManager)
            R.id.nav_extra -> NavigationManager.goToExtraFragment(supportFragmentManager)
            R.id.nav_dashboard -> NavigationManager.goToDashboardFragment(supportFragmentManager)
            R.id.nav_map -> NavigationManager.goToMapFragment(supportFragmentManager)
        }
        binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        when {
            binding.drawer.isDrawerOpen(GravityCompat.START) -> binding.drawer.closeDrawer(
                GravityCompat.START
            )
            supportFragmentManager.backStackEntryCount == 1 -> finish()
            else -> super.onBackPressed()
        }
    }

}