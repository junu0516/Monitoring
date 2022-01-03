/*=========================================================================================
    File Name: column.js
    Description: Chartjs column chart
    ----------------------------------------------------------------------------------------
    Item Name: Stack - Responsive Admin Theme
    Author: PIXINVENT
    Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/

// Column chart
// ------------------------------
$(window).on('load', function () {
  //Get the context of the Chart canvas element we want to select
  var ctx = $('#column-chart');

  // Chart Options
  var chartOptions = {
    // Elements options apply to all of the options unless overridden in a dataset
    // In this case, we are setting the border of each bar to be 2px wide and green
    elements: {
      rectangle: {
        borderWidth: 2,
        borderColor: 'rgb(0, 255, 0)',
        borderSkipped: 'bottom',
      },
    },
    responsive: true,
    maintainAspectRatio: false,
    responsiveAnimationDuration: 500,
    legend: {
      position: 'top',
    },
    scales: {
      xAxes: [
        {
          display: true,
          gridLines: {
            color: '#f3f3f3',
            drawTicks: false,
          },
          scaleLabel: {
            display: true,
          },
        },
      ],
      yAxes: [
        {
          display: true,
          gridLines: {
            color: '#f3f3f3',
            drawTicks: false,
          },
          scaleLabel: {
            display: true,
          },
        },
      ],
    },
    title: {
      display: true,
      text: 'Chart.js Bar Chart',
    },
  };

  // Chart Data
  var chartData = {
    labels: ['04-14', '04-15', '04-16', '04-17', '04-18', '04-19', '04-20'],
    datasets: [
      {
        label: '성공',
        data: [65, 59, 80, 81, 56, 70, 65],
        backgroundColor: '#16D39A',
        hoverBackgroundColor: 'rgba(22,211,154,.9)',
        borderColor: 'transparent',
      },
      {
        label: '실패',
        data: [28, 48, 40, 19, 11, 20, 12],
        backgroundColor: '#F98E76',
        hoverBackgroundColor: 'rgba(249,142,118,.9)',
        borderColor: 'transparent',
      },
    ],
  };

  var config = {
    type: 'bar',

    // Chart Options
    options: chartOptions,

    data: chartData,
  };

  // Create the chart
  var lineChart = new Chart(ctx, config);
});
