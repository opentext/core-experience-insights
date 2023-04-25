/**
 * Assorted Constants used by this webapp.
 * 
 * To use: 
 * import * as c from './path/to/constants'
 * 
 * CONSTANT_NAME
 * 
 */
export const isDev = (process.env.NODE_ENV === 'development')
export const ENV_REST_URI = 'apiBaseUri'
export const ENV_MESSAGES_KEY = 'messages'
export const DEFAULT_LOCALE = 'en'
export const ENV_ERROR_MESSAGE = 'errorMessage'

export const COLORS = [
    '#111b58',
    '#2e3d98',
    '#4f3690',
    '#00b8ba',
    '#09bcef',
    '#7e929f',
    '#e1e8f6',
    '#b4421a',
    '#a0006b'
]

export const QUERY_PATH = '/query'
export const QUERY_TYPE_STAGECOUNTS = 'stage_counts'

export const CHARTYPE_BAR = 'bar'
export const CHARTYPE_BAR_PATH = '/' + CHARTYPE_BAR

export const CHARTYPE_COLUMN = 'column'
export const CHARTYPE_COLUMN_PATH = '/' + CHARTYPE_COLUMN

export const CHARTYPE_DONUT = 'donut'
export const CHARTYPE_DONUT_PATH = '/' + CHARTYPE_DONUT

export const CHARTYPE_FUNNEL = 'funnel'
export const CHARTYPE_FUNNEL_PATH = '/' + CHARTYPE_FUNNEL

export const COLUMN_NAME_FOR_STAGE = 'stage'
export const COLUMN_NAME_FOR_STAGECOUNT = 'stagecount'

export const SESSION_EXPIRED_ERROR = 'session.has.expired'

// Default AJAX timeout in milliseconds
export const AJAX_TIMEOUT = 60000
