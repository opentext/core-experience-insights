/**
 * Assorted helper functions for the tests.
 * 
 * Be sure to copy config.sample.json to config.json and set parameters accordingly
 * 
 * NOTE: we can also support our .env pattern by including the following:
 * 
    const dotenv = require('dotenv')
    dotenv.config()
    
    process.env.DEV_REST_URL // for example
 *
 */
import config from '../../config'

/**
 * Test URL
 */
export const getPageURL = () => `${config.testURL}`
