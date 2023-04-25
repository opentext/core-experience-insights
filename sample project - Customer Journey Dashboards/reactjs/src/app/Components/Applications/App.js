import React from 'react'
import PropTypes from 'prop-types'

import {
    HashRouter as Router,
    Route,
    Switch
} from 'react-router-dom'

import { IntlProvider } from 'react-intl'

import MainWrapper from 'AppApps/MainWrapper'

function App(props) {
    const { locale, messages } = props
    return (        
        <IntlProvider defaultLocale='en' locale={locale} messages={messages}>           
            <Router>                        
                <Switch>
                    <Route path='/' component={MainWrapper} />
                </Switch>                        
            </Router>            
        </IntlProvider>        
    )
}

App.propTypes = {
    locale   : PropTypes.string,   
    messages : PropTypes.object,
}
export default App