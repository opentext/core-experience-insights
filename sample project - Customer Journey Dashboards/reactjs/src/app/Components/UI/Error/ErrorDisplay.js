import React, { Component } from 'react'
import { FormattedMessage } from 'react-intl'
import { SESSION_EXPIRED_ERROR} from '../../../constants'
import PropTypes from 'prop-types'
/**
 * For errors caught within React component
 * error boundaries.
 */
class ErrorDisplay extends Component {
   
    render() {
        const { errorClass, error, errorId, errorInfo, noHeader} = this.props
        if (error === SESSION_EXPIRED_ERROR) {
            return null
        }
        const errorIntl = errorId || 'error.display'
        return (
            <div className={errorClass}>
                <h2>{!noHeader && <FormattedMessage id={errorIntl} />}</h2>
                <p className='color-red'>
                    {error}
                </p>
                <p className="small">
                    {errorInfo && errorInfo.componentStack}
                </p>
            </div>
        )
    }
}
ErrorDisplay.propTypes = {
    errorClass : PropTypes.object,
    error      : PropTypes.string,
    errorId    : PropTypes.string,
    errorInfo  : PropTypes.string,
    noHeader   : PropTypes.string,
}
export default ErrorDisplay