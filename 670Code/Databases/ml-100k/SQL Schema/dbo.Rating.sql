CREATE TABLE [dbo].[Rating]
(
	[UserId] INT NOT NULL, 
    [MovieId] INT NOT NULL, 
    [Rating] INT NOT NULL, 
    [Timestamp] INT NULL, 
	CONSTRAINT Rating_PK PRIMARY KEY (UserId,MovieId),
	CONSTRAINT User_Id_FK FOREIGN KEY ( UserId ) REFERENCES [User] ( Id ),
	CONSTRAINT Movie_Id_FK FOREIGN KEY ( MovieId ) REFERENCES [Movie] ( Id )
)
