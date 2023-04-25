import React, { Component } from 'react'
import PropTypes from 'prop-types'

class Nav extends Component {
    
    render() {    
        const { intl } = this.props    
        return (            
            <nav className='navbar navbar-expand-md fixed-top has-bg-gradient ec-dark otxec-dark-bg'>
                <a                  
                    href='#'
                    style={{textDecoration: 'none'}} 
                    aria-label={intl.formatMessage({id: 'nav.brand.label'})}>               
                    <div className='pr-2 pl-2'>
                        <div className='ot-brand ot-brand--is-dark'>
                            <a className='ot-brand__logo'>
                                <img alt='OT2' src='/images/logo.svg' title='OpenText'/>
                            </a>
                            <div className='ot-brand__divider'>
                            </div>
                            <div className='ot-brand__primary'>Sample Report Application</div>                           
                        </div>
                    </div>
                </a>
            </nav>
        )
    }
}


Nav.propTypes = {
    intl : PropTypes.object
}
export default Nav