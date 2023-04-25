import React, { useRef } from 'react'
import PropTypes from 'prop-types'
import { injectIntl } from 'react-intl'

import Main from './Main'
import ErrorBoundary from 'AppUI/Error/ErrorBoundary'
import Nav from 'AppUI/Nav'


/**
 * Parent Component for our Application.
 */
function MainWrapper(props) {

    const wrapperRef = useRef(null) 
    
    const renderMainPage = () => {
        return (
            <ErrorBoundary>
                <div ref={wrapperRef} className='opentext-expctr has-bg-gradient ec-dark'>
                    <Nav {...props}/>
                    <div className='otxec-main-page'>                       
                        <Main wrapperRef={wrapperRef} {...props}/>
                    </div>
                </div>
            </ErrorBoundary>)
    }

    return  renderMainPage()
}

MainWrapper.propTypes = {
    intl    : PropTypes.object,
    history : PropTypes.object    
}

export default (injectIntl(MainWrapper))

