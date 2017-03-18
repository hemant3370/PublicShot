package hemant.com.publicshot.Dagger.Module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by HemantSingh on 18/03/17.
 */
@Module
public class RealmModule {
    @Provides
    @Singleton
    RealmConfiguration provideRealmConfiguration() {
        final RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded();
        return builder.build();
    }

    @Provides
    Realm provideDefaultRealm(RealmConfiguration config) {
        return Realm.getInstance(config);
    }
}
