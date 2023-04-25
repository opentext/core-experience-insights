/**
 * Error Handlers
 */

/**
 * Logger.
 * 
 * @param {*} errorMessage 
 */
const logError = (errorMessage) => {
    console.error(errorMessage)
    if (errorMessage.request) {
        console.error('http-error: ' + errorMessage.request.responseURL + ' responded with status=' + errorMessage.request.status)
    }
}

/**
 * Wrapper error message.
 * 
 * @param {*} status 
 * @param {*} msg 
 */
const getErrorField = (status, msg='Unable to complete your request, please try again') => {
    return {
        message : msg,
        status  : status
    }
}

/**
 * The generated error message.
 * 
 * @param {*} err 
 * @returns 
 */
export const returnErrorMessage = (err) => { 
    logError(err)  
    const errMessage = (err.response && err.response.data && err.response.data.messages) ?  err.response.data.messages[0] : 'Unknown error'
    const errorResponse = getErrorField(err.response.status, errMessage)
    return { 
        error : {
            message : errorResponse.message,
            status  : errorResponse.status
        }    
    }
}