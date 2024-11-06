import React, { Component } from 'react'
import { Container, Row, Col } from 'reactstrap'
import { FormattedMessage } from 'react-intl'
import PropTypes from 'prop-types'


import { getContextPath } from 'App/envloader'

/**
 * This is the destination page for server-side 404s, 500s, etc.
 */
class ErrorPage extends Component {
    
    render() {
        const { match } = this.props
        const errorType = (match && match.params) ? match.params.errorType : 404
        return (
            <Container fluid={true} className="homepage" role='main'>
                {//hidden h1 for accessibility
                }
                <h1 className='otxec-hide'><FormattedMessage id='title.autherror.page'/></h1>
                <Row>
                    <Col md='12'>
                        <div className='error-message'>
                            <FormattedMessage id={`error.${errorType}`}/>
                        </div>
                        <div className='error-sub-message'>
                            { (errorType === '503') ? <FormattedMessage id='error.outage.messsage'/> : <FormattedMessage id='error.administration.messsage'/>}
                        </div>
                    </Col>
                </Row>
                {errorType !== '503' && <Row className='error-buttons'>
                    <Col className='d-flex justify-content-end'>
                        <div className='mr-4'>
                            <div className='error-back-icon' onClick={()=> history.go(-1)}></div>
                            <div className='error-icon-message' onClick={()=> history.go(-1)}>
                                <FormattedMessage id='error.server.back'/>
                            </div>
                        </div>
                    </Col>
                    <Col className='d-flex'>
                        <div className='ml-4'>
                            <a style={{textDecoration: 'none'}} onClick={() => window.location = getContextPath() || '/'}>
                                <div className='error-home-icon'></div>
                                <div className='error-icon-message'>
                                    <FormattedMessage id='error.server.home'/>
                                </div>
                            </a>
                        </div>
                    </Col>
                </Row>}
            </Container>
        )
    }
}
ErrorPage.propTypes = {
    match : PropTypes.object,
}
export default ErrorPage