package `in`.workindia.rapidwebview

import android.content.Context

/**
 * Base client which is used to fetch information, such as the application context
 */
class RapidClient(private val appContext: Context) {

    companion object {
        private var mInstance: RapidClient? = null;

        /**
         * Check if RapidClient is initialised
         * @return Return `true` if instance is not null, `false` otherwise
         */
        @Synchronized
        @JvmStatic
        fun isInstanceInitialised(): Boolean {
            return null != mInstance
        }

        /**
         * Create RapidClient instance
         * @param context Context for runtime, generally expected to be `ApplicationContext`
         * @return RapidClient object
         * @throws NullPointerException if null `context` is passed
         */
        @Synchronized
        @JvmStatic
        fun createInstance(context: Context): RapidClient {
            if (null == context) {
                throw NullPointerException("RapidClient context cannot be null")
            }

            if (null == mInstance) {
                mInstance = RapidClient(context)
            }

            return mInstance as RapidClient
        }

        /**
         * Returns RapidClient instance
         * @return RapidClient object
         * @throws IllegalStateException if instance is not initialised via RapidClient.createInstance
         */
        @Synchronized
        @JvmStatic
        fun getInstance(): RapidClient {
            if (null == mInstance) {
                throw IllegalStateException("RapidClient.createInstance() needs to be called before RapidClient.getInstance()")
            }

            return mInstance as RapidClient
        }
    }

    fun getAppContext(): Context {
        return this.appContext
    }

}