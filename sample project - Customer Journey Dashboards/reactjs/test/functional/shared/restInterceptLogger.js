import { RequestHook } from 'testcafe'
import config from '../config'

const doLog = (config.interceptREST === true)

/**
 * Logs all REST traffic
 */
class RESTInterceptorLogger extends RequestHook {

    onRequest (event) {
        if (doLog) {
            console.log('onRequest: ')
            console.log('request.url ', event.requestOptions.path)
            console.log('request.header ', event.requestOptions.headers)
            if (event.requestOptions.proxy) {
                console.log('request.proxy ', event.requestOptions.proxy)
            }
        }
    }

    onResponse (event) {
        if (doLog) {
            console.log('response.header ', event.statusCode)
            console.log('response.header ', event.headers)
            console.log('response.body ', event.body)
        }
    }
}

export const restInterceptLogger = new RESTInterceptorLogger(/rest/, {
    includeHeaders : true,
    includeBody    : true
})
