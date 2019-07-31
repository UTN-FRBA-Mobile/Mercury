package ar.edu.utn.frba.mobile.a2019c1.mercury.widget
import android.widget.RemoteViewsService
import android.content.Intent


class WidgetService: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ListProvider(this.applicationContext, intent)
    }
}