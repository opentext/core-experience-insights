import React from 'react'
import ErrorDisplay from './ErrorDisplay' 
import PropTypes from 'prop-types'

class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props)
        this.state = { hasError: false }
    }
  
    static getDerivedStateFromError(error, errorInfo) {
        // Update state so the next render will show the fallback UI.
        return { 
            hasError : true,
            errorMsg : error,
            errorInfo }
    }
  
  
    render() {
        if (this.state.hasError) {
            //if it's dev just return our error display (easier for debugging since it doesn't change location)
            return <ErrorDisplay      
                errorClass='graph-workspace'
                error={this.state.errorMsg}
                errorInfo={this.state.errorInfo}
            />
        }
        return this.props.children 
    }
}

ErrorBoundary.propTypes = {
    children : PropTypes.object,
}
export default ErrorBoundary