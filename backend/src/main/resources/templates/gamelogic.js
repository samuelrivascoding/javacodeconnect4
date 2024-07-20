function handleClick(button) {
    const colIndex = parseInt(button.dataset.col); // Get the column index from the data-col attribute
    fetch(`/api/connect4/move`, {
      method: 'POST',
      body: JSON.stringify({ col: colIndex }) // Send chosen column index in the body
    })
    .then(response => response.json())
    .then(data => {
      // Update the UI based on the response (board, message, winner)
      updateBoard(data.board);
      document.getElementById('message').textContent = data.message;
      if (data.winner !== 0) {
        // Handle winner scenario (disable clicks, display winner message)
      }
    });
  }
  
  function updateBoard(boardData) {
    const boardContainer = document.getElementById('board-container');
    boardContainer.innerHTML = ''; // Clear existing board content
    // ... (similar logic as before to recreate the board based on boardData)
  }