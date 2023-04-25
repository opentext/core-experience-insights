/**
 * General loading utilities for the server-side variables.
 */
import _ from 'lodash'

import * as c from '../app/constants'
         
// CSRF token for use in the http api
export const getCSRF = () =>  (document.cookie) ? document.cookie.split('; ').find(row => row.startsWith('XSRF-TOKEN=')).split('=')[1] : ''

// clone loaded env values
const finalEnv = (typeof sampleUI != 'undefined') ? _.cloneDeep(sampleUI) : {}

export const getMessages = () => finalEnv[c.ENV_MESSAGES_KEY] && !c.isDev ? JSON.parse(finalEnv[c.ENV_MESSAGES_KEY]) : {}

export const getRestBaseURI = () => finalEnv[c.ENV_REST_URI]

export const getErrorMessage = () => finalEnv[c.ENV_ERROR_MESSAGE]
