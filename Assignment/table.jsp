<!DOCTYPE html>
<html>
<head>
    <title>Data Table with Filters</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
    <div class="container">
        <h2>Data Table with Filters</h2>
        <div class="row">
            <div class="col-sm-3">
                <label for="flightTiming">Flight Timing:</label>
                <select id="flightTiming" class="form-control">
                    <option value="">All</option>
                    <option value="Morning">Morning</option>
                    <option value="Afternoon">Afternoon</option>
                    <option value="Evening">Evening</option>
                    <option value="Night">Night</option>
                </select>
            </div>
            <div class="col-sm-3">
                <label for="flightName">Flight Name:</label>
                <input type="text" id="flightName" class="form-control">
            </div>
            <div class="col-sm-3">
                <label for="stopType">Stop:</label>
                <select id="stopType" class="form-control">
                    <option value="">All</option>
                    <option value="Direct">Direct Flight</option>
                    <option value="Indirect">Indirect Flight</option>
                </select>
            </div>
        </div>
        <br>
        <table id="dataTable" class="table table-bordered">
            <thead>
                <tr>
                    <th>Flight Time</th>
                    <th>Price</th>
                    <th>Flight Name</th>
                    <th>Stop</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>

    <script>
        $(document).ready(function() {
            loadDataTable();

            // Attach event listeners to filter elements
            $('#flightTiming, #flightName, #stopType').on('change', function() {
                loadDataTable();
            });
        });

        function loadDataTable() {
            var flightTiming = $('#flightTiming').val();
            var flightName = $('#flightName').val();
            var stopType = $('#stopType').val();

            $.ajax({
                url: 'FlightDataServlet',
                method: 'GET',
                data: {
                    flightTiming: flightTiming,
                    flightName: flightName,
                    stopType: stopType
                },
                success: function(data) {
                    var tbody = $('#dataTable tbody');
                    tbody.empty();

                    data.forEach(function(flight) {
                        var row = '<tr>' +
                            '<td>' + flight.flightTime + '</td>' +
                            '<td>' + flight.price + '</td>' +
                            '<td>' + flight.flightName + '</td>' +
                            '<td>' + flight.stopType + '</td>' +
                            '</tr>';
                        tbody.append(row);
                    });
                }
            });
        }
    </script>
</body>
</html>
