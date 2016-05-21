
  var day_data = [
    { "period": "2012-09-10", "mood": 12 },
    { "period": "2012-09-11", "mood": 15 },
    { "period": "2012-09-12", "mood": 20 },
    { "period": "2012-09-13", "mood": 25 },
    { "period": "2012-09-14", "mood": 10 }
  ];

Morris.Line({
// ID of the element in which to draw the chart.
  element: 'moodGraph',
// Chart data records -- each entry in this array corresponds to a point on
// the chart.
  data: day_data,
  // The name of the data record attribute that contains x-values.
  xkey: 'period',
  // A list of names of data record attributes that contain y-values.
  ykeys: ['mood'],
  // Labels for the ykeys -- will be displayed when you hover over the
  // chart.
  labels: ['Mood']
});

