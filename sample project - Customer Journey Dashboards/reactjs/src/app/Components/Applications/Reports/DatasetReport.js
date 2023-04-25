import React, { useState, useEffect } from 'react'
import BarChartIcon from '@material-ui/icons/BarChart'
import PropTypes from 'prop-types'
import DonutChartIcon from '@material-ui/icons/DonutLarge'
import { AiFillFunnelPlot } from 'react-icons/ai'
import { IconButton } from '@material-ui/core'
import { executeQuery } from 'App/api/sample'
import BarChart from 'AppUI/Charts/BarChart'
import {
    Container,
    FormGroup,
    Label,
    Input,
    Col,
    Card,
    CardBody,
    CardTitle,
} from 'reactstrap'
import { MemoryRouter as Router, Route, Switch, Link } from 'react-router-dom'
import FunnelChart from 'AppUI/Charts/FunnelChart'
import ColumnChart from 'AppUI/Charts/ColumnChart'
import DonutChart from 'AppUI/Charts/DonutChart'
import Message from 'AppUI/Message'
import LinearProgress from '@material-ui/core/LinearProgress'
import * as c from 'App/constants'

const DatasetReport = (props) => {
    const { intl, showErrorMessage } = props
    const listJourneysQuery = {
        select : [
            {
                field : 'journey_id',
            },
            {
                field : 'journey_name',
            },
        ],
        from    : 'otec_bd_journey_stage_details',
        groupBy : ['journey_id'],
    }

    const [journeys, setJourneys] = useState({
        data         : [],
        isDataLoaded : false,
    })
    useEffect(() => {
        setIsFetching(true)
        executeQuery(listJourneysQuery)
      .then((results) => {
          setIsFetching(false)
          setJourneys({ isDataLoaded: true, data: results.data.rows.sort((a, b) => a.journey_name > b.journey_name ? 1 : -1) })
      })
      .catch(handleErrorResponse)
    }, [])

    const [data, setData] = useState([])

    const [isFetching, setIsFetching] = useState(false)

    const handleErrorResponse = (error) => {
        let message = 'unable to get results from query api'
        if (error.response) {
            message = error.message
        } else if (error.request) {
            console.log(error.request)
        } else {
            // Something happened in setting up the request that triggered an Error
            console.log('Error', error.message)
        }
        setIsFetching(false)
        showErrorMessage(message)
    }

    const handleJourneySelect = (event) => {
        let journeyId = event.target.value
        const selectStageCountsQuery = {
            type : c.QUERY_TYPE_STAGECOUNTS,
            journeyId,
        }

        setIsFetching(true)
        showErrorMessage('')
        executeQuery(selectStageCountsQuery)
      .then((results) => {
          setData(results.data.rows.filter((r) => r.stagecount >= 0))
          setIsFetching(false)
      })
      .catch(handleErrorResponse)
    }

    const routes = [
        {
            path      : c.CHARTYPE_COLUMN_PATH,
            component : ColumnChart,
            menuIcon  : BarChartIcon,
        },
        {
            path      : c.CHARTYPE_BAR_PATH,
            component : BarChart,
            menuIcon  : BarchartHIcon,
        },
        {
            path      : c.CHARTYPE_DONUT_PATH,
            component : DonutChart,
            menuIcon  : DonutChartIcon,
        },
        {
            path      : c.CHARTYPE_FUNNEL_PATH,
            component : FunnelChart,
            menuIcon  : FunnelChartIcon,
        },
        {
            path      : '/',
            component : ColumnChart,
            menuIcon  : BarChartIcon,
        },
    ]

    const hasJourneyData = journeys.isDataLoaded && journeys.data.length > 0

    return (
        <div>
            {isFetching && (
                <div>
                    <LinearProgress />
                </div>
            )}
            <div className='report-panel'>
                <div>
                    <Card className='m-2'>
                        <Router>
                            <CardBody className='bg-light border'>
                                <CardTitle tag='h5'>
                                    {intl.formatMessage({
                                        id : 'section.sample.report.title',
                                    })}
                                </CardTitle>
                                <div className='d-flex justify-content-end'>
                                    <FormGroup row className='d-flex justify-content-end mb-auto'>
                                        <Col className='d-flex justify-content-end' sm={12} md={3} >
                                            <Label for='journeySelect' size='sm'>
                                                {intl.formatMessage({
                                                    id : 'section.sample.report.journey-report.select.label',
                                                })}
                                            </Label>
                                        </Col>
                                        <Col className='d-flex justify-content-end' sm={12} md={5} >
                                                                                    

                                            <Input
                                                bsSize='sm'
                                                id='journeySelect'
                                                name='journey'
                                                placeholder='sm'
                                                type='select'
                                                onChange={handleJourneySelect}
                                            >
                                                <option
                                                    key={null}
                                                    value={null}
                                                    style={{ fontStyle: 'italic' }}
                                                >
                                                    {intl.formatMessage({
                                                        id : 'section.sample.report.journey-report.select.message',
                                                    })}
                                                </option>
                                                {hasJourneyData &&
                                                    journeys.data.map((journey) => (
                                                        <option
                                                            key={journey.journey_id}
                                                            value={journey.journey_id}
                                                        >
                                                            {journey.journey_name}
                                                        </option>
                                                    ))}
                                            </Input>                                            
                                        </Col>
                                        <Col className='d-flex justify-content-end' sm={12} md={4}   >
                                            {routes
                          .filter((route) => route.path != '/')
                          .map((route, i) => (
                              <IconButton
                                  key={i}
                                  component={Link}
                                  to={route.path}
                                  className='otxec-chart-icon'
                              >
                                  <route.menuIcon />
                              </IconButton>
                          ))}
                                        </Col>
                                    </FormGroup>
                                </div>
                                
                            </CardBody>
                            <CardBody className='m-auto p-6'>
                                <Container>
                                    {data.length > 0 && (
                                        <Switch>
                                            {routes.map((route, i) => (
                                                <Route
                                                    key={i}
                                                    path={route.path}
                                                    render={() => (
                                                        <route.component
                                                            data={data}
                                                            categories={c.COLUMN_NAME_FOR_STAGE}
                                                            values={c.COLUMN_NAME_FOR_STAGECOUNT}
                                                        />
                                                    )}
                                                />
                                            ))}
                                        </Switch>
                                    )}

                                    <div className='p-2 m-auto d-flex justify-content-center'>
                                        {isFetching &&
                      !journeys.isDataLoaded &&
                      journeys.data.length == 0 && (
                                            <Message
                                                text={intl.formatMessage({
                                                    id : 'section.sample.report.journey-report.loading.message',
                                                })}
                                                type='info'
                                            />
                                        )}
                                        {journeys.isDataLoaded && journeys.data.length == 0 && (
                                            <Message
                                                text={intl.formatMessage({
                                                    id : 'section.sample.report.journey-report.nodataset.message',
                                                })}
                                                type='info'
                                            />
                                        )}
                                        {hasJourneyData &&
                      data.length == 0 && (
                                            <Message
                                                text={intl.formatMessage({
                                                    id : 'section.sample.report.journey-report.content.message',
                                                })}
                                                type='primary'
                                            />
                                        )}
                                    </div>
                                </Container>
                            </CardBody>
                        </Router>
                    </Card>
                </div>
            </div>
        </div>
    )
}

const BarchartHIcon = () => {
    return (
        <BarChartIcon
            style={{
                transform : 'rotate(90deg)',
            }}
        />
    )
}
const FunnelChartIcon = () => {
    return (
        <AiFillFunnelPlot
            style={{
                width    : '38px',
                height   : '38px',
                minWidth : '20px'
            }}
        />
    )
}

DatasetReport.propTypes = {
    intl             : PropTypes.object,
    showErrorMessage : PropTypes.func,
}
export default DatasetReport
