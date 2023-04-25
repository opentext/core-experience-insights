
import axios from 'axios'
import {AJAX_TIMEOUT} from 'App/constants'

import { getLocale } from '../localebuilder'

/** for handling aborted requests mid-flight (which mitigates React component leaks) */
export const getAxiosCancelToken = () => axios.CancelToken.source()


class HttpClient {

    constructor() {
        this.httpClient = axios.create({})
        this.setDefaults()
        this.setInterceptors()
    }

    getClient() {
        return this.httpClient
    }
    
    setDefaults() {
        this.httpClient.defaults.timeout = AJAX_TIMEOUT
        // Always include the locale on REST requests
        this.httpClient.defaults.headers.common['Accept-Language'] = getLocale() // otherwise it's the browser default
        this.httpClient.defaults.headers.post['Content-Type'] = 'application/json'
        this.httpClient.defaults.headers.get['Content-Type'] = 'application/json'
        this.httpClient.defaults.headers.common = {
            'X-Requested-With' : 'XMLHttpRequest'
        }
    }

    /**
    * Intercept handles logging of specific errors.
    * This will trigger an api's "catch" handler
    */
    setInterceptors = async () => { 
      
        this.httpClient.interceptors.response.use(
            response => {
                return Promise.resolve(response)
            },
            error => {
                if (axios.isCancel(error)) {
                    return error
                }
                const { response: { status, headers } = {} } = error
                console.log("response headers for an error", error)
                const isServiceAvailabilityError = !!(status === 503  && headers.xhr_request_error === 'Unavailable_Error')
                const isCSRFError = !!(status === 403 && headers.xhr_request_error && headers.xhr_request_error === 'CSRF_Error')
                const isSessionError = status === 401
                if (isCSRFError || isSessionError) {
                    //alert('session expired')                    
                    return Promise.reject(error)
                }
                if (isServiceAvailabilityError) {                  
                    return Promise.reject(error)
                }
                return Promise.reject(error)
            })
    }
}

// Singleton factory-ish
let httpClient = null
export const getHttpClient = () => {
    if (!httpClient) {
        httpClient = new HttpClient().getClient()
    }
    return httpClient
}

export const getHttp = () => {
    return new HttpClient().getClient()
}


// Primary axios client object
export default getHttpClient