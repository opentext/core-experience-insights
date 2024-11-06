/**
 * General loading utilities for the server-side variables.
 */
import _ from 'lodash'

import * as c from '../app/constants'

// clone loaded env values
const finalEnv = (typeof sampleUI != 'undefined') ? _.cloneDeep(sampleUI) : {}

export const getMessages = () => finalEnv[c.ENV_MESSAGES_KEY] && !c.isDev ? JSON.parse(finalEnv[c.ENV_MESSAGES_KEY]) : {}

export const getRestBaseURI = () => getContextPath()  + finalEnv[c.ENV_REST_URI]

export const getErrorMessage = () => finalEnv[c.ENV_ERROR_MESSAGE]

export const getContextPath = () => finalEnv[c.ENV_CONTEXT_PATH] || ''
