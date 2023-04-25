/**
 * admin.js handles the backend REST calls via AJAX for administration features.
 */

import { getHttpClient } from './http'
import { getRestBaseURI } from '../envloader'
import { QUERY_PATH} from '../constants'

/**
 * 
 * @param {*} query 
 * @param {*} params 
 * @returns 
 */
export const executeQuery = (query,params) => {
    const url = getRestBaseURI() + QUERY_PATH
    return getHttpClient()
    .post(url, query, {
        headers : {
            'Content-Type' : 'application/json',
        },
        params : params
    })
    .then((response) => {
        return { 
            data : response.data,
            page : response.data['_page'],
            next : response.data['@next']
        }
    })
}
